import os
from dotenv import load_dotenv

load_dotenv()
# API
APIS = {
    "cats": {
        "url": "https://api.thecatapi.com/v1/images/search",
        "key": os.getenv("CAT_API_KEY"),
        "images_dir": "cat_images",
        "metadata_file": "cats_metadata.json",
        "name": "котиков"
    },
    "dogs": {
        "url": "https://api.thedogapi.com/v1/images/search",
        "key": os.getenv("DOG_API_KEY"),
        "images_dir": "dog_images",
        "metadata_file": "dogs_metadata.json",
        "name": "собачек"
    }
}

# Общие настройки
TARGET_COUNT = 1050
LIMIT_PER_REQUEST = 100
REQUEST_DELAY = 0.3
MAX_FAILURES = 10

# Пути к данным
CAT_IMAGES_DIR = "cat_images"
DOG_IMAGES_DIR = "dog_images"

# Параметры изображений
IMAGE_SIZE = 128
MEAN = [0.485, 0.456, 0.406]
STD = [0.229, 0.224, 0.225]

# Параметры обучения
BATCH_SIZE = 32
NUM_WORKERS = 0
LEARNING_RATE = 1e-3
WEIGHT_DECAY = 1e-4

# CNN с нуля
CNN_EPOCHS = 15

# Transfer Learning
TRANSFER_EPOCHS = 15

# Разделение данных
VAL_SPLIT = 0.15
TEST_SPLIT = 0.15
RANDOM_SEED = 42

# TensorBoard
LOG_DIR = "runs"