import logging
from dataclasses import asdict
from typing import Dict, List, Union, Optional, Callable, Tuple
from urllib.parse import urljoin
import pandas as pd
import requests
from bs4 import BeautifulSoup
import time
import random

from .config import (
    MAIN_REALTY_URL,
    MAX_MAIN_PAGES,
    DELAY_RANGE,
    OUTPUT_FILE_PATH,
    OUTPUT_ENCODING,
    WRITE_ROWS_IMMEDIATELY, START_MAIN_PAGE,
)
from .captcha import fetch_url_with_captcha
from .listings import ObjectListing

logger = logging.getLogger(__name__)


def give_object_links_from_main_page_html(html: str) -> List[Dict[str, Union[str, bool]]]:
    """Возвращает список ссылок (div.it-list div.j-item) на карточки объектов (div.it-list-item-in) с main страницы каталога недвижимости;
    Дополнительно помечает тег "СРОЧНО" у карточки ("urgent_tag") по наличию 'selected' в классе div.j-item
    """
    soup = BeautifulSoup(html, "html.parser")
    results: List[Dict[str, Union[str, bool]]] = []
    for j_item in soup.select("div.it-list div.j-item"):
        a_tag = j_item.select_one("div.it-list-item-in a[href]")
        if a_tag:
            results.append({
                "href": a_tag["href"],
                "urgent_tag": ("selected" in (j_item.get("class") or []))
            })
    return results


def _sleep_delay(delay_range: Optional[Tuple[float, float]]) -> None:
    """Случайная задержка в диапазоне"""
    if delay_range is None:
        return
    try:
        lo, hi = delay_range
        if hi < lo:
            lo, hi = hi, lo
        lo = max(0.0, lo)
        if hi > 0:
            time.sleep(random.uniform(lo, hi))
    except (TypeError, ValueError, OverflowError):
        pass


def crawl_dataset(start_main_page: int = 1, max_main_pages: int = 2, *, delay_range: Optional[Tuple[float, float]] = None, on_row: Optional[Callable[[Dict], None]] = None) -> List[Dict]:
    """Обходит главные страницы каталога и собирает карточки объектов:
    - max_main_pages: количество главных страниц для обхода (начиная с 1).
    - delay_range: (min_delay, max_delay) в секундах между карточками
    - on_row: стриминговый режим записи строк
    """
    records: List[Dict] = []
    written_count: int = 0
    with requests.Session() as session:
        for main_page_number in range(start_main_page, max_main_pages + 1):
            url = f"{MAIN_REALTY_URL}?page={main_page_number}"
            logger.info(f"Обработка главной страницы №{main_page_number}: {url}")
            try:
                html = fetch_url_with_captcha(session, url, referer=MAIN_REALTY_URL)
            except requests.RequestException as e:
                logger.warning(f"Ошибка сети при загрузке главной страницы {url}: {e}")
                continue

            links = give_object_links_from_main_page_html(html)

            for idx, link in enumerate(links, start=1):
                href = link.get("href") if isinstance(link, dict) else str(link)
                urgent_tag = bool(link.get("urgent_tag")) if isinstance(link, dict) else False
                if not href:
                    continue
                object_url = urljoin(MAIN_REALTY_URL, href)
                logger.info(f" №{main_page_number} [{idx}/{len(links)}] Обработка: {object_url}")
                try:
                    object_html = fetch_url_with_captcha(session, object_url, referer=url)
                except requests.RequestException as e:
                    logger.warning(f"Ошибка сети карточки {object_url}: {e}")
                    _sleep_delay(delay_range)
                    continue

                try:
                    listing = ObjectListing.from_html(object_html, url=object_url)
                    row = {**asdict(listing), "urgent_tag": urgent_tag}
                    if on_row is not None:
                        try:
                            on_row(row)
                            written_count += 1
                        except (OSError, ValueError, TypeError) as e:
                            logger.error(f"Не удалось записать строку в CSV: {e}")
                    else:
                        records.append(row)
                except (ValueError, AttributeError, KeyError) as e:
                    logger.error(f"Ошибка парсинга карточки {object_url}: {e}")
                finally:
                    _sleep_delay(delay_range)

            logger.info(
                f"Итого записей собрано: {len(records) if on_row is None else written_count}"
            )
    return records


def save_dataset(rows: List[Dict], file_path: str = OUTPUT_FILE_PATH, *, encoding: str = OUTPUT_ENCODING) -> None:
    """Сохраняет строки в CSV-файл;
    - rows: список словарей из ObjectListing
    - file_path: путь к датасету
    """
    if not rows:
        logger.info("Нет данных для сохранения")
        return
    if pd is not None:
        df = pd.DataFrame.from_records(rows)
        df.to_csv(file_path, index=False, encoding=encoding)
    else:
        import csv
        fieldnames = sorted({k for row in rows for k in row.keys()})
        with open(file_path, "w", encoding=encoding, newline="") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(rows)
    logger.info(f"Датасет сохранён в {file_path}. Строк: {len(rows)}")


def _stream_fieldnames() -> List[str]:
    from dataclasses import fields as dc_fields
    fns = [f.name for f in dc_fields(ObjectListing)]
    if "urgent_tag" not in fns:
        fns.append("urgent_tag")
    return fns


def crawl():
    """Точка входа для сбора и записи датасета:
    WRITE_ROWS_IMMEDIATELY=True -> строки пишутся последовательно в CSV
    WRITE_ROWS_IMMEDIATELY=False -> строки собираются в память и сохраняются одним файлом
    """
    import csv
    logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(name)s: %(message)s")
    if WRITE_ROWS_IMMEDIATELY:
        fieldnames = _stream_fieldnames()
        written_total = 0
        with open(OUTPUT_FILE_PATH, "w", encoding=OUTPUT_ENCODING, newline="") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()

            def _on_row(row: Dict) -> None:
                nonlocal written_total
                writer.writerow({k: row.get(k, "") for k in fieldnames})
                f.flush()
                written_total += 1

            crawl_dataset(start_main_page=START_MAIN_PAGE, max_main_pages=MAX_MAIN_PAGES, delay_range=DELAY_RANGE, on_row=_on_row)
        logger.info(f"Стриминговая запись завершена: {OUTPUT_FILE_PATH}. Строк записано: {written_total}")
    else:
        rows = crawl_dataset(start_main_page=START_MAIN_PAGE, max_main_pages=MAX_MAIN_PAGES, delay_range=DELAY_RANGE)
        save_dataset(rows, file_path=OUTPUT_FILE_PATH, encoding=OUTPUT_ENCODING)
        logger.info(f"Пакетная запись завершена: {OUTPUT_FILE_PATH}. Строк записано: {len(rows)}")
