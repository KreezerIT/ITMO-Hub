import logging
import re
from typing import Optional, Dict, Tuple
import requests
from bs4 import BeautifulSoup, FeatureNotFound
from urllib.parse import urljoin

from .utils import _copy_headers, _title_text, _norm

logger = logging.getLogger(__name__)


def _parse_math_captcha_and_payload(html: str) -> Tuple[Optional[str], Dict[str, str]]:
    """Парсит капчу из HTML формы с математическим примером;
    Ищет форму с <label> вида "A + B = ?", собирает скрытые input[type=hidden];
    возвращает (action, payload) с ответом в поле 'captcha' как сумму A+B
    """
    soup = BeautifulSoup(html, "html.parser")
    form = soup.find("form")
    if not form:
        return None, {}

    label = form.find("label") or soup.find("label")
    question = label.get_text(strip=True) if label else ""
    m = re.search(r"(\d+)\s*\+\s*(\d+)", question)
    if not m:
        return None, {}

    a, b = int(m.group(1)), int(m.group(2))
    answer = str(a + b)

    payload: Dict[str, str] = {}
    for inp in form.find_all("input", {"type": "hidden"}):
        name = inp.get("name")
        value = inp.get("value", "")
        if name:
            payload[name] = value
    payload["captcha"] = answer

    action = form.get("action")
    return action, payload


def _is_captcha_page(html: str) -> bool:
    """Проверяет страницу на наличие капчи"""
    try:
        soup = BeautifulSoup(html or "", "html.parser")
    except (FeatureNotFound, TypeError, ValueError):
        return False
    title = _title_text(soup)
    if title and "проверка на бота" in title.lower():
        return True
    text = (_norm(soup.get_text(" ", strip=True)) or "").lower()
    if any(k in text for k in ("проверка на бота", "вы не робот", "защита от роботов", "подтвердите, что вы не бот")):
        return True
    _action, payload = _parse_math_captcha_and_payload(html)
    if payload.get("captcha") is not None:
        return True
    return False


def fetch_url_with_captcha(session: requests.Session, url: str, *, referer: Optional[str] = None, timeout: int = 30) -> str:
    """Загружает URL;
    GET + При наличии капчи готовит payload <- (_parse_math_captcha_and_payload)
    и повторно делает GET, устанавливая Referer на action/response URL (без фактического POST, просто GET с Referer);
    Возвращает HTML-текст ответа.
    Все запросы выполняются в рамках переданной сессии (TCP-соединения переиспользуются, куки сохраняются);
    Заголовки задаются на уровне отдельных запросов;
    Возможность таймаута на каждый GET
    """
    resp = session.get(url, headers=_copy_headers(referer), timeout=timeout)
    html = resp.text

    if not _is_captcha_page(html):
        return html

    logger.info("Страница с капчей")

    action, payload = _parse_math_captcha_and_payload(html)
    if not payload:
        logger.warning("Не распознана форма капчи или payload. Возвращен исходный HTML")
        return html

    post_url = urljoin(resp.url, action) if action else resp.url

    try:
        soup_tmp = BeautifulSoup(html, "html.parser")
        form_tmp = soup_tmp.find("form")
        form_method = (form_tmp.get("method", "post") if form_tmp else "post").strip().lower()
    except (AttributeError, TypeError, ValueError, FeatureNotFound):
        form_method = "post"

    try:
        post_headers = _copy_headers(resp.url)
        if form_method == "get":
            post_resp = session.get(post_url, params=payload, headers=post_headers, timeout=timeout, allow_redirects=True)
        else:
            post_resp = session.post(post_url, data=payload, headers=post_headers, timeout=timeout, allow_redirects=True)
        logger.info("Капча отправлена, статус: %s", post_resp.status_code)
    except requests.RequestException as e:
        logger.warning("Ошибка сети при отправке решения капчи: %s", e)
        return html

    try:
        check_resp = session.get(url, headers=_copy_headers(post_url), timeout=timeout)
        last_html = check_resp.text
    except requests.RequestException as e:
        logger.warning("Ошибка сети при повторной загрузке страницы после капчи: %s", e)
        return html

    return last_html
