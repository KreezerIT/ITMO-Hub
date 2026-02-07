"""
Data storage handler for incremental saving.
"""
import csv
import os
from typing import List
from dataclasses import dataclass
import logging

logger = logging.getLogger(__name__)


@dataclass
class TextSample:
    text: str
    label: str
    author: str = ""
    category: str = ""
    original_id: str = ""


class DataStorage:
    """Saving of data to CSV."""

    def __init__(self, filepath: str):
        self.filepath = filepath
        self.temp_filepath = filepath.replace('.csv', '_temp.csv')
        os.makedirs(os.path.dirname(filepath) if os.path.dirname(filepath) else '.', exist_ok=True)

        # Загрузка существующих данных
        self.existing_samples = self.load_existing()
        if self.existing_samples:
            logger.info(f"Loaded {len(self.existing_samples)} existing samples")

    def load_existing(self) -> List[TextSample]:
        samples = []

        # Try main file first, then temp file
        for path in [self.filepath, self.temp_filepath]:
            if os.path.exists(path):
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        reader = csv.DictReader(f)
                        for row in reader:
                            samples.append(TextSample(
                                text=row['text'],
                                label=row['label'],
                                author=row.get('author', ''),
                                category=row.get('category', ''),
                                original_id=row.get('original_id', '')
                            ))
                    logger.info(f"Loaded data from {path}")
                    break
                except Exception as e:
                    logger.error(f"Error loading {path}: {e}")

        return samples

    def count_by_label(self, label: str) -> int:
        return sum(1 for s in self.existing_samples if s.label == label)

    def save_incremental(self, new_samples: List[TextSample], source: str):
        all_samples = self.existing_samples + new_samples

        # Временный файл для записи
        fieldnames = ['text', 'label', 'author', 'category', 'original_id']

        with open(self.temp_filepath, 'w', newline='', encoding='utf-8') as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, quoting=csv.QUOTE_ALL)
            writer.writeheader()

            for sample in all_samples:
                writer.writerow({
                    'text': sample.text,
                    'label': sample.label,
                    'author': sample.author,
                    'category': sample.category,
                    'original_id': sample.original_id
                })

        self.existing_samples = all_samples
        logger.info(f"Saved {len(new_samples)} {source} samples (total: {len(all_samples)})")

    # Выгрузка в основной датасет (.csv)
    def finalize(self):
        if os.path.exists(self.temp_filepath):
            os.replace(self.temp_filepath, self.filepath)
            logger.info(f"Finalized data saved to {self.filepath}")