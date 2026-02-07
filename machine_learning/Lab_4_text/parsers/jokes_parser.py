import time
import requests
from typing import List, Set, Optional
from tqdm import tqdm
import logging

import config
from data_storage import TextSample

logger = logging.getLogger(__name__)


class JokesCollector:
    """Collector for jokes from JokeAPI (IDs 0-318)"""

    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
            'Accept': 'application/json',
        })
        self.collected_ids: Set[int] = set()

    def fetch_jokes_batch(self, start_id: int, end_id: int) -> List[dict]:
        try:
            # Ensure we don't exceed max ID
            end_id = min(end_id, config.JOKES_MAX_ID)

            params = {
                'idRange': f'{start_id}-{end_id}',
                'amount': min(10, end_id - start_id + 1)
            }

            response = self.session.get(
                config.JOKES_API_URL,
                params=params,
                timeout=config.REQUEST_TIMEOUT
            )

            if response.status_code != 200:
                logger.debug(f"Status {response.status_code} for range {start_id}-{end_id}")
                return []

            data = response.json()

            if data.get("error"):
                return []

            jokes = data.get("jokes", [])

            if not jokes and ("joke" in data or "setup" in data):
                jokes = [data]

            return jokes

        except requests.RequestException as e:
            logger.warning(f"Request failed for range {start_id}-{end_id}: {e}")
            return []
        except Exception as e:
            logger.error(f"Unexpected error for range {start_id}-{end_id}: {e}")
            return []

    def process_joke(self, joke_data: dict) -> Optional[TextSample]:
        """Convert joke data to TextSample."""
        joke_id = joke_data.get("id")

        # Скип повторок
        if joke_id in self.collected_ids:
            return None

        if joke_data.get("type") == "twopart":
            setup = joke_data.get("setup", "").strip()
            delivery = joke_data.get("delivery", "").strip()
            text = f"{setup} {delivery}"
        else:
            text = joke_data.get("joke", "").strip()

        if not text or len(text) < 10:
            return None

        self.collected_ids.add(joke_id)

        return TextSample(
            text=text,
            label="joke",
            author="",
            category=joke_data.get("category", "Unknown"),
            original_id=str(joke_id)
        )

    def collect(self) -> List[TextSample]:
        samples: List[TextSample] = []

        logger.info(f"Collecting jokes from IDs 0-{config.JOKES_MAX_ID}")

        # Шкала загрузки
        pbar = tqdm(
            total=config.JOKES_MAX_ID + 1,
            desc="Collecting jokes"
        )

        batch_size = 10
        current_id = 0

        while current_id <= config.JOKES_MAX_ID:
            end_id = min(current_id + batch_size - 1, config.JOKES_MAX_ID)

            jokes_data = self.fetch_jokes_batch(current_id, end_id)

            for joke_data in jokes_data:
                sample = self.process_joke(joke_data)
                if sample:
                    samples.append(sample)

            pbar.update(end_id - current_id + 1)
            current_id = end_id + 1

            # Ждем ограничение по скорости запросов
            time.sleep(config.REQUEST_DELAY)

        pbar.close()

        logger.info(f"Collected {len(samples)} jokes (out of {config.JOKES_MAX_ID + 1} IDs)")

        return samples


def collect_jokes(target: Optional[int] = None) -> List[TextSample]:
    if target and target > config.JOKES_MAX_ID + 1:
        logger.warning(f"Target {target} exceeds available jokes ({config.JOKES_MAX_ID + 1})")

    collector = JokesCollector()
    return collector.collect()


if __name__ == "__main__":
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(levelname)s - %(message)s'
    )

    logger.info("Testing JokeAPI collection")
    jokes = collect_jokes()

    print(f"\n" + "-" * 70)
    print(f"Total jokes collected: {len(jokes)}")

    if jokes:
        categories = {}
        for joke in jokes:
            cat = joke.category
            categories[cat] = categories.get(cat, 0) + 1

        print("\nCategory distribution:")
        for cat, count in sorted(categories.items(), key=lambda x: -x[1]):
            print(f"  {cat}: {count}")

        print("\nSample jokes:")
        for i, joke in enumerate(jokes[:3], 1):
            text = joke.text[:100] + ".." if len(joke.text) > 100 else joke.text
            print(f"{i}. [{joke.category}] {text}")

    print("-" * 70)