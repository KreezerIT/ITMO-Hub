"""
Main parser script for all collectors.

Usege:
    python main_parser.py           # Collect all types
    python main_parser.py quotes    # Collect only quotes
    python main_parser.py jokes     # Collect only jokes
    python main_parser.py facts     # Collect only facts
"""
import sys
import logging
import config
from data_storage import DataStorage
from parsers.quotes_parser import collect_quotes
from parsers.jokes_parser import collect_jokes
from parsers.facts_parser import collect_facts

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


def main(sources=None):
    logger.info("Data collecting starter")
    logger.info("Targets:")
    for source, count in config.SAMPLES_CONFIG.items():
        logger.info(f"  {source}s: {count}")
    logger.info(f"Output: {config.RAW_DATA_PATH}")
    logger.info("-" * 70)

    storage = DataStorage(config.RAW_DATA_PATH)

    existing_counts = {
        "quote": storage.count_by_label("quote"),
        "joke": storage.count_by_label("joke"),
        "fact": storage.count_by_label("fact")
    }

    for label, count in existing_counts.items():
        if count > 0:
            logger.info(f"Found existing {label}s: {count}")

    collectors = {
        "quotes": ("quote", collect_quotes, config.SAMPLES_CONFIG.get("quote", 1000)),
        "jokes": ("joke", collect_jokes, config.SAMPLES_CONFIG.get("joke", 319)),
        "facts": ("fact", collect_facts, config.SAMPLES_CONFIG.get("fact", 1000))
    }

    if sources:
        collectors = {k: v for k, v in collectors.items() if k in sources}

    for source_name, (label, collect_func, target) in collectors.items():
        if existing_counts[label] >= target:
            logger.info(f"Already have enough {source_name}: {existing_counts[label]}/{target}")
            continue

        # Подсчет сколько еще осталось собрать
        needed = target - existing_counts[label]
        if needed <= 0:
            continue

        logger.info(f"\nCollecting {needed} more {source_name} (target: {target})")

        try:
            samples = collect_func(needed)
            if samples:
                storage.save_incremental(samples, source_name)
                logger.info(f"Successfully collected {len(samples)} {source_name}")
            else:
                logger.warning(f"No {source_name} collected")

        except KeyboardInterrupt:
            logger.warning("\nInterrupted. Saving progress")
            break
        except Exception as e:
            logger.error(f"Error collecting {source_name}: {e}")
            logger.info("Continuing with next source")
            continue

    storage.finalize()

    logger.info("-" * 70)
    logger.info("Data collection resulst")
    logger.info("-" * 70)

    all_samples = storage.existing_samples
    if all_samples:
        logger.info(f"Total samples: {len(all_samples)}")

        print("\nSource      | Collected | Target   | Status")
        print("------------|-----------|----------|--------")

        for source_name, (label, _, target) in collectors.items():
            count = sum(1 for s in all_samples if s.label == label)
            percentage = (count / target * 100) if target > 0 else 0
            status = "✓" if count >= target else f"{percentage:.0f}%"
            print(f"{source_name:<11} | {count:>9} | {target:>8} | {status}")

        logger.info(f"\nSaved to: {config.RAW_DATA_PATH}")
    else:
        logger.error("No samples collected")

    logger.info("-" * 70)


if __name__ == "__main__":
    if len(sys.argv) > 1:
        sources = sys.argv[1:]
        valid_sources = ["quotes", "jokes", "facts"]
        sources = [s for s in sources if s in valid_sources]
        if sources:
            logger.info(f"Collecting only: {', '.join(sources)}")
            main(sources)
        else:
            print("Usage: python main_parser.py [quotes] [jokes] [facts]")
            sys.exit(1)
