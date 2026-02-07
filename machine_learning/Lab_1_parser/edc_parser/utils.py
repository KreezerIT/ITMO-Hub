import re
from typing import Optional, List, Tuple, Dict, Iterator
from bs4 import BeautifulSoup

from .config import BASE_HEADERS

def _title_text(soup: BeautifulSoup) -> Optional[str]:
    """Возвращает нормализованный текст тега <title>"""
    return soup.title.get_text(' ', strip=True) if soup.title else None

def _descr_text(soup: BeautifulSoup) -> Optional[str]:
    """Возвращает нормализованный текст описания карточки (div.vw-descr)"""
    descr = soup.find('div', class_='vw-descr')
    return descr.get_text(' ', strip=True) if descr else None

def _copy_headers(referer: Optional[str] = None) -> Dict[str, str]:
    """Копирует базовые HTTP-заголовки и добавляет Referer если нужно"""
    headers = dict(BASE_HEADERS)
    if referer:
        headers["Referer"] = referer
    return headers

def _norm(text: Optional[str]) -> str:
    """Нормализует пробелы и неразрывные пробелы в строке, приводя последовательности пробелов к одному"""
    if not text:
        return ""
    t = str(text).replace("\xa0", " ")
    return " ".join(t.split())

def _extract_first_int(text: Optional[str]) -> Optional[int]:
    """Извлекает первое целое число из строки, проходя слева направо"""
    if not text:
        return None
    s = _norm(text)
    buf = []
    for ch in s:
        if ch.isdigit() and ch != '²':
            buf.append(ch)
        elif buf:
            break
    return int("".join(buf)) if buf else None

def _extract_last_int_before(s: str, idx: int) -> Optional[int]:
    """Возвращает последнее число, встретившееся в строке слева от позиции idx"""
    if idx <= 0:
        return None
    matches = re.findall(r"\d+", _norm(s[:idx]))
    return int(matches[-1]) if matches else None


def _has_sqm_unit(s: str, end_idx: int) -> bool:
    """Проверяет, что сразу после числа указана единица площади в квадратных метрах;
    Допускает обозначения: (м², м2, м^2, квм, квм2, квметр, кв.м, кв.м2, кв. м, кв м, кв.м.);
    Одиночная 'м' без индикатора квадрата (²/2/^2) - не считается м²
    """
    tail = s[end_idx:end_idx + 24].lower()
    compact = tail.replace(" ", "").replace(".", "").replace(",", "")

    # Слитные обозначения
    if compact.startswith(("м²", "м2", "м^2", "квм", "квм2", "квметр", "кв.м", "кв.м2")):
        return True

    # Раздельные обозначения ('кв. м', 'кв м') в ближайшем окне после числа
    if "кв" in tail:
        kv_pos = tail.find("кв")
        m_pos = tail.find("м", kv_pos + 2)
        if 0 <= kv_pos < m_pos != -1 and (m_pos - kv_pos) <= 6:
            return True

    # Одиночная 'м' только если далее явно идёт индикатор квадрата (²,2,^, ^ 2)
    i = 0
    while i < len(tail) and tail[i] == ' ':
        i += 1
    if i < len(tail) and tail[i] == 'м':
        j = i + 1
        while j < len(tail) and tail[j] == ' ':
            j += 1
        if j < len(tail) and tail[j] in ('²', '2', '^'):
            if tail[j] == '^':
                j2 = j + 1
                while j2 < len(tail) and tail[j2] == ' ':
                    j2 += 1
                if j2 < len(tail) and tail[j2] == '2':
                    return True
                return False
            return True
        return False

    return False


def _match_area_number(text: str) -> Optional[float]:
    """Ищет площадь в м² в свободном тексте;
    Допускает обозначения: (м², м2, м^2, квм, квм2, квметр, кв.м, кв.м2, кв. м, кв м, кв.м.) <- [def _has_sqm_unit];
    Исключает одиночное "м" без индикатора квадрата
    """
    if not text:
        return None
    s = _norm(text)
    i = 0
    while i < len(s):
        if s[i].isdigit():
            j = i
            dot_used = False
            num_buf: List[str] = []
            while j < len(s) and (s[j].isdigit() or (s[j] in ",. " and not dot_used)):
                if s[j].isdigit():
                    num_buf.append(s[j])
                elif s[j] in ",.":
                    num_buf.append('.')
                    dot_used = True
                elif s[j] == ' ' and not num_buf:
                    pass
                j += 1
            num_str = "".join(num_buf)
            if num_str:
                try:
                    val = float(num_str)
                except ValueError:
                    val = None
                if val is not None and _has_sqm_unit(s, j):
                    return val
            i = j
        else:
            i += 1
    return None


def _match_sot_area_number(text: str) -> Optional[float]:
    """Возвращает площадь в м², если в тексте найдена площадь в сотках (1 сотка = 100 м²) ТОЛЬКО если в карточке ранее не найдена площадь в явно указанных м²;
    Ищем число, за которым следует обозначение соток;
    Допустимые обозначения: 'сот', 'сот.', 'сотк*', 'соток' ...
    """
    if not text:
        return None
    s = _norm(text).lower()
    i = 0
    while i < len(s):
        if s[i].isdigit():
            j = i
            dot_used = False
            num_buf: List[str] = []
            while j < len(s) and (s[j].isdigit() or (s[j] in '., ' and not dot_used)):
                if s[j].isdigit():
                    num_buf.append(s[j])
                elif s[j] in '.,':
                    num_buf.append('.')
                    dot_used = True
                elif s[j] == ' ' and not num_buf:
                    pass
                j += 1
            num_str = ''.join(num_buf)
            k = j
            while k < len(s) and s[k] in (' ', '\u00a0'):
                k += 1
            if num_str and k + 2 < len(s) and s[k] == 'с' and s[k+1] == 'о' and s[k+2] == 'т':
                # Проверка допустимого окончания (пунктуация/пробел) либо формы с 'к' ('сотк*','соток')
                k2 = k + 3
                next_char = s[k2] if k2 < len(s) else ''
                ok = False
                if k2 >= len(s):
                    ok = True
                elif next_char in ('.', ' ', '\u00a0', ',', ')', ']', ';', ':', '-', '/', '"', "'"):
                    ok = True
                elif next_char == 'к':
                    ok = True
                elif next_char == 'о' and (k2 + 1 < len(s) and s[k2+1] == 'к'):
                    ok = True
                if ok:
                    try:
                        val = float(num_str)
                        return val * 100.0
                    except ValueError:
                        return None
            i = j
        else:
            i += 1
    return None


def _near_contains_etazh_word(s: str, start: int, window: int = 16) -> bool:
    """Возвращает True, если в окне строки после start встречается указание на этажность: 'эт', 'этаж', 'этажн'"""
    tail = s[start:start + window].lower()
    return ("эт" in tail) or ("этаж" in tail) or ("этажн" in tail)


def _deep_match_floors_total_number(text: str) -> Optional[int]:
    """Возвращает общее количество этажей, если распознано поблизости от пометок этажности;
      - 'этажност*:' с числом справа;
      - '... этажей: N' / 'N этажей';
      - формат X/Y рядом с пометкой этажа (X - этаж, Y - этажность);
      - 'эт.' (число слева);
      - 'Y-этажн*'
    """
    if not text:
        return None
    s = _norm(text).lower()

    # 0) 'этажност*:'
    pos = s.find('этажност')
    while pos != -1:
        j = pos + len('этажност')
        while j < len(s) and s[j] in ('ь', 'и'):
            j += 1
        while j < len(s) and s[j] in [':', ' ', '\u00a0', '-', '–', '-', ',', ';']:
            j += 1
        win = s[j:j + 12]
        v = _extract_first_int(win)
        if v is not None and 1 <= v <= 100:
            return v
        pos = s.find('этажност', pos + 1)

    # 1) 'этажей:' (сначала ищем число слева, потом справа)
    pos = s.find('этажей')
    while pos != -1:
        end = pos + len('этажей')
        j = end
        while j < len(s) and s[j] in [':', ' ', '\u00a0', '-', '–', '-', ',', ';']:
            j += 1
        win = s[j:j + 10]
        v_right = _extract_first_int(win)
        if v_right is not None and 1 <= v_right <= 100:
            return v_right
        v_left = _extract_last_int_before(s, pos)
        if v_left is not None and 1 <= v_left <= 100:
            return v_left
        pos = s.find('этажей', pos + 1)

    # 2) X/Y
    slash_pos = s.find('/')
    if slash_pos != -1:
        right_num = _extract_first_int(s[slash_pos + 1:])
        if right_num is not None and _near_contains_etazh_word(s, slash_pos):
            return right_num

    # 3) 'эт.'
    idx = s.find(' эт.')
    while idx != -1:
        pre = s[max(0, idx - 8):idx]
        if ' на ' in pre:
            idx = s.find(' эт.', idx + 1)
            continue
        val = _extract_last_int_before(s, idx)
        if val is not None and 1 <= val <= 100:
            return val
        idx = s.find(' эт.', idx + 1)

    # 4) 'Y-этажн*'
    pos = s.find('этажн')
    while pos != -1:
        val = _extract_last_int_before(s, pos)
        if val is not None and 1 <= val <= 100:
            return val
        pos = s.find('этажн', pos + 1)

    # 5) 'Y этажей' (без ':')
    pos = s.find(' этажей')
    while pos != -1:
        val = _extract_last_int_before(s, pos)
        if val is not None and 1 <= val <= 100:
            return val
        pos = s.find(' этажей', pos + 1)

    return None


def _parse_floor_total_from_text(text: str) -> Tuple[Optional[int], Optional[int]]:
    """Парсит этаж и общее количество этажей из текста;
    Принимает обозначения: 'X/Y этаж', 'на X эт.', 'Y этажей', 'Y-этажн*',
    извлекает числа рядом с 'этаж'/'этаже'/'эт.'
    """
    if not text:
        return None, None
    s = _norm(text).lower()

    floor: Optional[int] = None
    total: Optional[int] = None

    def _first_int(st: str) -> Optional[int]:
        m = re.search(r"\d+", st)
        return int(m.group()) if m else None

    # 1) 'X/Y этаж'
    slash_pos = s.find('/')
    if slash_pos != -1 and _near_contains_etazh_word(s, slash_pos):
        left = _extract_last_int_before(s, slash_pos)
        right = _first_int(s[slash_pos + 1:])
        if left or right:
            return left, right

    # 2) Число рядом с 'эт*'
    for key in ["этаж", "этаже", "эт."]:
        pos = s.find(key)
        while pos != -1:
            # игнорируем "этажн..."
            if s.startswith("этажн", pos):
                pos = s.find(key, pos + 1)
                continue

            # справа
            after = s[pos + len(key):pos + len(key) + 8]
            v_after = _first_int(after)
            if v_after and 1 <= v_after <= 100 and "м" not in after:
                floor = v_after
                break

            # слева
            v_before = _extract_last_int_before(s, pos)
            if v_before and 1 <= v_before <= 100:
                # Исключение '... м 5 этаж'
                if not s[max(0, pos-4):pos].endswith("м "):
                    floor = v_before
                    break
            pos = s.find(key, pos + 1)
        if floor is not None:
            break

    # 3) Этажность
    if total is None:
        total = _deep_match_floors_total_number(s)

    return floor, total


def _iter_dynprops(soup: BeautifulSoup) -> Iterator[Tuple[str, str]]:
    """Итератор по парам (label_text_lower, value_text) для .vw-dynprops-item"""
    for item in soup.select('.vw-dynprops-item'):
        label = item.select_one('.vw-dynprops-item-attr')
        value = item.select_one('.vw-dynprops-item-val')
        label_text = (label.get_text(' ', strip=True) if label else '').lower()
        value_text = value.get_text(' ', strip=True) if value else ''
        yield label_text, value_text


def _text_mentions_studio(title_text: Optional[str]) -> bool:
    """Поиск упоминания студии в тексте: 'студ*'/'studio' """
    if not title_text:
        return False
    s = _norm(title_text).lower()
    if 'studio' in s:
        return True
    return 'студи' in s


def _match_rooms_before_keywords(s: str) -> Optional[int]:
    """Ищет число перед ключевыми словами о комнатах"""
    for key in ("комнат", "комната", "комнаты", "комн"):
        pos = s.find(key)
        while pos != -1:
            val = _extract_last_int_before(s, pos)
            if val is not None and 1 <= val <= 10:
                return val
            pos = s.find(key, pos + 1)
    return None


def _match_rooms_number(text: str) -> Optional[int]:
    """Ищет количество комнат в элементах title и dynprops"""
    if not text:
        return None
    s = _norm(text).lower()

    # Явные пометки комнат
    val = _match_rooms_before_keywords(s)
    if val is not None:
        return val

    # Краткая форма ('X-к')
    pos = 0
    while True:
        pos = s.find("-к", pos)
        if pos == -1:
            break
        val = _extract_last_int_before(s, pos)
        if val is not None and 1 <= val <= 10:
            return val
        pos += 2

    return None
