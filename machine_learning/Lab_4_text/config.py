import os

# PATHS
DATA_DIR = "data"
RAW_DATA_PATH = os.path.join(DATA_DIR, "dataset.csv")
TEMP_DATA_PATH = os.path.join(DATA_DIR, "dataset_temp.csv")
VOCAB_PATH = os.path.join(DATA_DIR, "vocab.json")
MODEL_PATH = os.path.join(DATA_DIR, "model.pt")
TENSORBOARD_LOG_DIR = "runs"

# API
# Quotes: zenquotes.io - LIMIT 5 requests per 30 seconds, 50 quotes per request
QUOTES_API_URL = "https://zenquotes.io/api/quotes"
QUOTES_BATCH_SIZE = 50
QUOTES_REQUESTS_PER_PERIOD = 5
QUOTES_RATE_LIMIT_PERIOD = 30

# Jokes: JokeAPI - IDs from 0 to 318 available, LIMIT 10 jokes per request
JOKES_API_URL = "https://v2.jokeapi.dev/joke/Any"
JOKES_BATCH_SIZE = 10
JOKES_MAX_ID = 318  # Actual maximum available ID

# Facts: uselessfacts - one fact per request
FACTS_API_URL = "https://uselessfacts.jsph.pl/api/v2/facts/random"

# DATA COLLECTION SETTINGS
SAMPLES_CONFIG = {
    "quote": 1000,
    "joke": 319,
    "fact": 1000
}
SAMPLES_PER_SOURCE = 1000

# Request settings
REQUEST_DELAY = 0.2
REQUEST_TIMEOUT = 10
MAX_RETRIES = 3
BACKOFF_MULTIPLIER = 2.0

# Text settings
MAX_VOCAB_SIZE = 5000
MIN_WORD_FREQ = 2
MAX_SEQ_LENGTH = 150

# Special tokens
PAD_TOKEN = "<PAD>"
UNK_TOKEN = "<UNK>"
PAD_IDX = 0
UNK_IDX = 1

# Model Architecture
EMBEDDING_DIM = 128
HIDDEN_DIM = 256
NUM_LSTM_LAYERS = 2
BIDIRECTIONAL = True
DROPOUT_RATE = 0.4

# Classifier head
FC_HIDDEN_DIMS = [256, 128, 64]

# TRAINING HYPERPARAMETERS
BATCH_SIZE = 64
LEARNING_RATE = 1e-3
WEIGHT_DECAY = 1e-5
NUM_EPOCHS = 12
EARLY_STOPPING_PATIENCE = 3

# Data split
TRAIN_RATIO = 0.7
VAL_RATIO = 0.15
TEST_RATIO = 0.15

RANDOM_SEED = 42
DEVICE = "cuda"

# Class Labels
LABEL_TO_IDX = {
    "quote": 0,
    "joke": 1,
    "fact": 2
}
IDX_TO_LABEL = {v: k for k, v in LABEL_TO_IDX.items()}
NUM_CLASSES = len(LABEL_TO_IDX)