import os, io, time, random
import requests
from PIL import Image, UnidentifiedImageError
from tqdm import tqdm

OUT_DIR = "dataset_pokemon"
SIZE = (48, 48)
os.makedirs(OUT_DIR, exist_ok=True)

session = requests.Session()
session.headers.update({"User-Agent": "Mozilla/5.0 (dataset scraper for ML lab)"})

BASE = "https://pokeapi.co/api/v2"

def get_json(url, retries=5):
    for attempt in range(retries):
        r = session.get(url, timeout=20)
        if r.status_code == 200:
            return r.json()

        # лимит/сбой - подождать и повторить
        if r.status_code in (429, 500, 502, 503, 504):
            time.sleep(1.5 * (attempt + 1) + random.random())
            continue
        return None
    return None

def download_image(url, retries=5):
    for attempt in range(retries):
        r = session.get(url, timeout=20)
        if r.status_code == 200:
            ctype = (r.headers.get("Content-Type") or "").lower()
            if ctype.startswith("image/"):
                return r.content

            # пришло не изображение
            return None
        if r.status_code in (429, 500, 502, 503, 504):
            time.sleep(1.5 * (attempt + 1) + random.random())
            continue
        return None
    return None

meta = get_json(f"{BASE}/pokemon?limit=1")
count = meta["count"]

saved, bad = 0, 0

for pid in tqdm(range(1, count + 1)):
    j = get_json(f"{BASE}/pokemon/{pid}")
    if not j:
        bad += 1
        continue

    url = j["sprites"]["front_default"]
    if not url:
        continue

    img_bytes = download_image(url)
    if not img_bytes:
        bad += 1
        continue

    try:
        img = Image.open(io.BytesIO(img_bytes)).convert("RGBA")
        img = img.resize(SIZE, Image.Resampling.NEAREST)
        img.save(os.path.join(OUT_DIR, f"{pid:04d}.png"))
        saved += 1
    except (UnidentifiedImageError, OSError):
        bad += 1
        continue

print("Saved:", saved, "Bad/Skipped:", bad)