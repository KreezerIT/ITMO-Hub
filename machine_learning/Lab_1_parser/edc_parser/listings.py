from dataclasses import dataclass
from typing import Optional, Tuple
from urllib.parse import urljoin, urlparse, unquote
from bs4 import BeautifulSoup
from datetime import datetime, timedelta

from .utils import (
    _title_text,
    _descr_text,
    _iter_dynprops,
    _match_area_number,
    _match_sot_area_number,
    _parse_floor_total_from_text,
    _deep_match_floors_total_number,
    _extract_first_int,
    _text_mentions_studio,
    _match_rooms_number,
)

@dataclass
class ObjectListing:
    url: str
    city: Optional[str]
    deal_type: Optional[str]
    object_type: Optional[str]
    rooms: Optional[int]
    floor: Optional[int]
    floors_total: Optional[int]
    area_m2: Optional[float]
    price: Optional[int]
    currency: Optional[str]
    price_per_m2: Optional[float]
    creation_date: Optional[str]
    images_folder_url: Optional[str]

    @staticmethod
    def _parse_price_and_currency(soup: BeautifulSoup) -> Tuple[Optional[int], Optional[str]]:
        """Ищет цену и валюту в блоке div.vw-price-box;
        Валюта обязательна и может быть только одной из доступных в форме добавления объявления:
            RUB, BYN, UAH, KZT, MDL, UZS, KGS.
        Формат: <число>[пробел]<валюта> (500 RUB) ИЛИ '<число>[пробел]'*<валюта> (500 000 RUB)
        """
        for sp in soup.select('div.vw-price-box span.vw-price-num'):
            txt = (sp.get_text(' ', strip=True) or '').strip()
            if not txt:
                return None, None
            parts = txt.split(' ')
            if len(parts) < 2:
                return None, None
            currency = parts[-1].upper()
            num_parts = parts[:-1]
            try:
                price = int(''.join(num_parts))
            except ValueError:
                return None, None
            return price, currency
        return None, None

    @staticmethod
    def _parse_rooms(soup: BeautifulSoup, *, object_type: Optional[str] = None) -> Optional[int]:
        """Определяет количество комнат для ЖИЛЫХ типов по приоритету источников: title -> dynprops -> description;
        Если явного числа в object_type 'apartments' нет, но встречается упоминание студии, возвращает 1;
        """
        if object_type not in ("apartments", "rooms"):
            return None

        # 1) title
        title = _title_text(soup)
        if title:
            v = _match_rooms_number(title)
            if v is not None:
                return v
        studio_title = (object_type == 'apartments') and _text_mentions_studio(title)

        # 2) vw-dynprops
        for label_text, value_text in _iter_dynprops(soup):
            if any(k in label_text for k in ['комнат', 'кол-во комнат', 'количество комнат']):
                v = _match_rooms_number(value_text) or _extract_first_int(value_text)
                if v is not None and 1 <= v <= 10:
                    return v
                if object_type == 'apartments':
                    vt = value_text.lower()
                    if _text_mentions_studio(vt):
                        return 1

        # 3) description
        descr_text = _descr_text(soup)
        if descr_text:
            s = descr_text.lower()
            from .utils import _match_rooms_before_keywords
            v = _match_rooms_before_keywords(s)
            if v is not None:
                return v

        # 4) дефолт для студий
        if studio_title:
            return 1
        return None

    @staticmethod
    def _parse_area_m2(soup: BeautifulSoup, *, object_type: Optional[str] = None) -> Optional[float]:
        """Сначала ищет площадь в м², если нет - поиск в сотках и конвертация (1 сотка = 100 м²);
        Приоритет источников: title -> vw-dynprops -> description;
        Для 'apartments'/'houses' игнорирует лейблы участка, для 'land' - учитывается
        """

        # title
        title = _title_text(soup)
        if title:
            # м²
            area = _match_area_number(title)
            if area is not None:
                return area

            # сотки
            area_m2 = _match_sot_area_number(title)
            if area_m2 is not None:
                return area_m2

        # dynprops
        dynprops_values = []
        for label_text, value_text in _iter_dynprops(soup):
            if 'площад' in label_text:
                if object_type == 'land':
                    # Для земли берём и 'площадь участка', и любые площади
                    dynprops_values.append(value_text)
                else:
                    if not ('участ' in label_text or 'земл' in label_text):
                        # Для остальных типов не берём участок/землю
                        dynprops_values.append(value_text)

        for value_text in dynprops_values:
            area = _match_area_number(value_text)
            if area is not None:
                return area

        # description
        descr_text = _descr_text(soup)
        if descr_text:
            area = _match_area_number(descr_text)
            if area is not None:
                return area


        # 2) сотки -> м²
        # dynprops
        for value_text in dynprops_values:
            area_m2 = _match_sot_area_number(value_text)
            if area_m2 is not None:
                return area_m2

        # description
        if descr_text:
            area_m2 = _match_sot_area_number(descr_text)
            if area_m2 is not None:
                return area_m2
        return None

    @staticmethod
    def _parse_floor_and_total(soup: BeautifulSoup) -> Tuple[Optional[int], Optional[int]]:
        """Парсит текущий этаж и общее количество этажей по приоритету: title -> dynprops -> description;
        Допускает обозначения: 'X/Y', 'Этажность:', 'Этажей:', 'эт.', 'X-этажн*';
        """
        # 1) title
        floor: Optional[int] = None
        total: Optional[int] = None
        title = _title_text(soup)
        if title:
            f0, t0 = _parse_floor_total_from_text(title)
            if f0 is not None:
                floor = f0
            if t0 is not None:
                total = t0

        # 2) dynprops: сборка и этажа, и этажности, дополняя недостающее
        for label_text, value_text in _iter_dynprops(soup):
            # Очевидные лейблы этажности
            if any(k in label_text for k in [
                'этажность', 'всего этаж', 'кол-во этаж', 'количество этаж', 'этажей'
            ]):
                if total is None:
                    v = _deep_match_floors_total_number(value_text) or _extract_first_int(value_text)
                    if v is not None and 1 <= v <= 100:
                        total = v
                continue

            # Очевидные лейблы текущего этажа
            if 'этаж' in label_text and 'этажей' not in label_text:
                vt = value_text.strip()
                # 'X/Y' без "этаж" в самом значении
                if '/' in vt and (floor is None or total is None):
                    left, right = vt.split('/', 1)
                    from .utils import _extract_first_int as _first
                    f = _first(left)
                    t = _first(right)
                    if f is not None and 1 <= f <= 100 and floor is None:
                        floor = f
                    if t is not None and 1 <= t <= 100 and total is None:
                        total = t

                # 'X/Y' -> общий парсер
                if floor is None or total is None:
                    f, t = _parse_floor_total_from_text(vt)
                    if f is not None and floor is None:
                        floor = f
                    if t is not None and total is None:
                        total = t

                # Очевидное одиночное число в значении при лейбле "Этаж"
                if floor is None:
                    f1 = _extract_first_int(vt)
                    if f1 is not None and 1 <= f1 <= 100:
                        floor = f1

        # 3) description
        if floor is None or total is None:
            descr_text = _descr_text(soup)
            if descr_text:
                f, t = _parse_floor_total_from_text(descr_text)
                if f is not None and floor is None:
                    floor = f
                if t is not None and total is None:
                    total = t
        return floor, total

    @staticmethod
    def _compute_price_per_m2(price: Optional[int], area_m2: Optional[float]) -> Optional[float]:
        """Возвращает цену за м² (price/area_m2) в той же валюте, что и price"""
        if price and area_m2 and area_m2 > 0:
            return price / area_m2
        return None

    @staticmethod
    def _find_images_folder_url(soup: BeautifulSoup, base_url: str) -> Optional[str]:
        """Ищет первый элемент с атрибутом data-thumb и возвращает URL папки с изображениями"""
        el = soup.find(attrs={"data-thumb": True})
        if not el:
            return None
        raw = el.get("data-thumb")
        if not raw:
            return None
        img_url = urljoin(base_url, raw)
        parsed = urlparse(img_url)
        path = parsed.path or '/'
        if not path.endswith('/'):
            if '/' in path:
                folder_path = path.rsplit('/', 1)[0]
            else:
                folder_path = ''
        else:
            folder_path = path
        if not folder_path:
            folder_path = '/'
        if not folder_path.endswith('/'):
            folder_path += '/'
        return f"{parsed.scheme}://{parsed.netloc}{folder_path}"

    @staticmethod
    def _parse_city_from_url(page_url: str) -> Optional[str]:
        """Извлекает название города из URL: часть после '/ru/' до 'real-estate' без 'commercial';
        Форматирует название: с заглавной буквы и пробелами
        """
        parsed = urlparse(page_url)
        parts = [p for p in parsed.path.split('/') if p]
        city_t: Optional[str] = None
        try:
            idx_ru = parts.index('ru')
        except ValueError:
            return None
        for i in range(idx_ru + 1, len(parts)):
            part = parts[i]
            if part not in {"ru", "real-estate"}:
                city_t = part
                break
        if not city_t:
            return None
        city_t = unquote(city_t)
        city = ' '.join(s.capitalize() for s in city_t.replace('-', ' ').split())
        return city or None

    @staticmethod
    def _parse_deal_and_object_from_url(page_url: str) -> Tuple[Optional[str], Optional[str]]:
        """Возвращает (deal_type, object_type) из URL;
        Основной формат: /real-estate/{deal}/{?one-day}/{object}/...;
        Поддержка шаблонов вида '{object}-{deal}' ('shop-rent'...)
        """
        parsed = urlparse(page_url)
        parts = [p for p in parsed.path.split('/') if p]
        try:
            idx = parts.index('real-estate')
        except ValueError:
            idx = -1
        if idx != -1:
            i = idx + 1
            if i < len(parts) and parts[i] == 'commercial':
                i += 1
            if i >= len(parts):
                return None, None
            raw_deal = parts[i]
            i += 1
            deal = 'sell' if raw_deal == 'sale' else raw_deal
            if i < len(parts) and parts[i] == 'one-day':
                i += 1
            obj = parts[i] if i < len(parts) else None
            return deal, obj
        return None, None

    @staticmethod
    def _parse_creation_date(soup: BeautifulSoup) -> Optional[str]:
        """Ищет дату создания объявления в блоке div.l-page-item-r-info.d-none.d-md-block span.nowrap;
        Парсит текст вида 'Создано: 12 марта 2020', 'Создано: Сегодня'/'Вчера';
        Формат готовой даты: 'DD.MM.YYYY'
        """
        for sp in soup.select('div.l-page-item-r-info.d-none.d-md-block span.nowrap'):
            txt = (sp.get_text(' ', strip=True) or '').strip()
            if not txt:
                return "None"
            low = txt.lower()
            if 'создано:' in low:
                if 'сегодня' in low:
                    now = datetime.now()
                    return f"{now.day:02d}.{now.month:02d}.{now.year:02d}"
                elif 'вчера' in low:
                    yesterday = datetime.now() - timedelta(days=1)
                    return f"{yesterday.day:02d}.{yesterday.month:02d}.{yesterday.year}"
                val = txt.split(':', 1)[1].strip().split(' ')
                day = val[0]
                month = {
                    'января': '01',
                    'февраля': '02',
                    'марта': '03',
                    'апреля': '04',
                    'мая': '05',
                    'июня': '06',
                    'июля': '07',
                    'августа': '08',
                    'сентября': '09',
                    'октября': '10',
                    'ноября': '11',
                    'декабря': '12',
                }.get(val[1].lower(), '00')
                current_year = datetime.now().year
                if len(val) == 2:
                    return f"{int(day):02d}.{month}.{current_year}"
                elif len(val) == 3:
                    year = val[2]
                    return f"{int(day):02d}.{month}.{year}"
        return None

    @classmethod
    def from_html(cls, html: str, *, url: str) -> "ObjectListing":
        """Строит ObjectListing из HTML карточки и URL"""
        soup = BeautifulSoup(html, "html.parser")
        deal_type, object_type = cls._parse_deal_and_object_from_url(url)
        city = cls._parse_city_from_url(url)
        rooms = cls._parse_rooms(soup, object_type=object_type)
        rooms = None if object_type == "houses" and rooms is not None else rooms
        floor_pair, floors_total_pair = cls._parse_floor_and_total(soup)
        floors_total = None if object_type == "land" else floors_total_pair
        floor = None if object_type in ("houses", "building", "land") else floor_pair
        area_m2 = cls._parse_area_m2(soup, object_type=object_type)
        price, currency = cls._parse_price_and_currency(soup)
        price_per_m2 = cls._compute_price_per_m2(price, area_m2)
        creation_date = cls._parse_creation_date(soup)
        images_folder_url = cls._find_images_folder_url(soup, url)
        return cls(
            url=url,
            city=city,
            deal_type=deal_type,
            object_type=object_type,
            rooms=rooms,
            floor=floor,
            floors_total=floors_total,
            area_m2=area_m2,
            price=price,
            currency=currency,
            price_per_m2=price_per_m2,
            creation_date=creation_date,
            images_folder_url=images_folder_url,
        )
