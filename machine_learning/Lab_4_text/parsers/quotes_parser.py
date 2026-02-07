import time
import hashlib
import requests
from typing import List, Dict
from tqdm import tqdm
import logging

import config
from data_storage import TextSample

logger = logging.getLogger(__name__)


class QuotesCollector:
    """
    Collector for quotes from zenquotes.io
    API limits: 5 requests per 30 seconds, 50 quotes per request
    """

    def __init__(self, num_samples: int):
        self.num_samples = num_samples
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
            'Accept': 'application/json',
        })
        self.request_count = 0
        self.period_start = time.time()

    def wait_for_rate_limit(self):
        """Handle rate limiting: 5 requests per 30 seconds."""
        if self.request_count >= config.QUOTES_REQUESTS_PER_PERIOD:
            elapsed = time.time() - self.period_start
            if elapsed < config.QUOTES_RATE_LIMIT_PERIOD:
                wait_time = config.QUOTES_RATE_LIMIT_PERIOD - elapsed + 1
                logger.info(f"Rate limit reached. Waiting {wait_time:.0f} seconds")
                time.sleep(wait_time)

            self.request_count = 0
            self.period_start = time.time()

    def collect(self) -> List[TextSample]:
        samples: Dict[str, TextSample] = {}
        pbar = tqdm(total=self.num_samples, desc="Collecting quotes")

        rate_limit_failures = 0
        max_rate_limit_failures = 5

        while len(samples) < self.num_samples:
            # Проверка ограничения запросов перед новым запросом
            self.wait_for_rate_limit()

            try:
                response = self.session.get(
                    config.QUOTES_API_URL,
                    timeout=config.REQUEST_TIMEOUT
                )

                if response.status_code == 429:
                    rate_limit_failures += 1
                    if rate_limit_failures >= max_rate_limit_failures:
                        logger.warning(f"Too many rate limits. Got {len(samples)} quotes.")
                        break
                    wait_time = 35
                    logger.info(f"Got rate limited. Waiting {wait_time} seconds")
                    time.sleep(wait_time)
                    self.request_count = 0
                    self.period_start = time.time()
                    continue

                response.raise_for_status()
                data = response.json()

            except Exception as e:
                logger.warning(f"Request failed: {e}")
                time.sleep(5)
                continue

            rate_limit_failures = 0
            self.request_count += 1

            if isinstance(data, list):
                for quote_data in data:
                    if len(samples) >= self.num_samples:
                        break

                    text = quote_data.get("q", "").strip()
                    author = quote_data.get("a", "Unknown")

                    # Скип 'zenquotes.io' и мусора
                    if "zenquotes.io" in text.lower() or len(text) < 10:
                        continue

                    if text:
                        unique_id = hashlib.md5(f"{text}{author}".encode()).hexdigest()[:8]

                        if unique_id not in samples:
                            samples[unique_id] = TextSample(
                                text=text,
                                label="quote",
                                author=author,
                                category="",
                                original_id=unique_id
                            )
                            pbar.update(1)

            time.sleep(config.REQUEST_DELAY)

        pbar.close()
        return list(samples.values())


def collect_quotes(target: int = config.SAMPLES_PER_SOURCE) -> List[TextSample]:
    collector = QuotesCollector(target)
    return collector.collect()


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    quotes = collect_quotes(50)
    print(f"Collected {len(quotes)} quotes")