import time
import requests
from typing import List, Dict
from tqdm import tqdm
import logging

import config
from data_storage import TextSample

logger = logging.getLogger(__name__)


class FactsCollector:
    """Collector for random facts from uselessfacts.jsph.pl"""

    def __init__(self, num_samples: int):
        self.num_samples = num_samples
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
            'Accept': 'application/json',
        })

    def get_random_fact(self) -> dict:
        try:
            response = self.session.get(
                config.FACTS_API_URL,
                timeout=config.REQUEST_TIMEOUT
            )
            response.raise_for_status()
            return response.json()
        except Exception as e:
            logger.debug(f"Failed to get fact: {e}")
            return None

    def collect(self) -> List[TextSample]:
        samples: Dict[str, TextSample] = {}
        pbar = tqdm(total=self.num_samples, desc="Collecting facts")

        consecutive_failures = 0
        max_failures = 30

        while len(samples) < self.num_samples:
            if consecutive_failures >= max_failures:
                logger.error(f"Too many failures. Got {len(samples)} facts.")
                break

            data = self.get_random_fact()

            if not data:
                consecutive_failures += 1
                time.sleep(config.REQUEST_DELAY * 2)
                continue

            consecutive_failures = 0
            fact_id = data.get("id", "")

            if fact_id and fact_id not in samples:
                text = data.get("text", "").strip()
                if text:
                    samples[fact_id] = TextSample(
                        text=text,
                        label="fact",
                        author=data.get("source", ""),
                        category="",
                        original_id=str(fact_id)
                    )
                    pbar.update(1)

            time.sleep(config.REQUEST_DELAY)

        pbar.close()
        return list(samples.values())


def collect_facts(target: int = config.SAMPLES_PER_SOURCE) -> List[TextSample]:
    collector = FactsCollector(target)
    return collector.collect()


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    facts = collect_facts(50)
    print(f"Collected {len(facts)} facts")