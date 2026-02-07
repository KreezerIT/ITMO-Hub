MAIN_REALTY_URL = "https://edc.sale/ru/real-estate/"
BASE_HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
}

OUTPUT_FILE_PATH = "data/dataset.csv"
OUTPUT_ENCODING = "utf-8-sig"

# сколько главных страниц обходить (начиная с 1, одна страница = 10 записей)
MAX_MAIN_PAGES = 3551
# с какой главной страницы начинать обход (по дефолту с 1)
START_MAIN_PAGE = 1
# диапазон задержки между карточками (a.b, c.d) в секундах
DELAY_RANGE = None

# True -> строки пишутся последовательно в CSV
# False -> строки собираются в память и сохраняются одним файлом
WRITE_ROWS_IMMEDIATELY = True
