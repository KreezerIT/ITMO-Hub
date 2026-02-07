import argparse
import requests
import os
import time
import json
from pathlib import Path
from typing import Set, Dict, List

from config import APIS, TARGET_COUNT, LIMIT_PER_REQUEST, REQUEST_DELAY, MAX_FAILURES


def load_existing_metadata(filepath: str) -> Dict:
    if os.path.exists(filepath):
        try:
            with open(filepath, 'r') as f:
                content = f.read().strip()
                if content:
                    return json.loads(content)
        except json.JSONDecodeError:
            print(f"WARNING: {filepath} повреждён, создаю новый")
    return {"images": []}


def save_metadata(metadata: Dict, filepath: str) -> None:
    with open(filepath, 'w') as f:
        json.dump(metadata, f, indent=2)


def get_downloaded_ids(metadata: Dict) -> Set[str]:
    return {img["id"] for img in metadata["images"]}


def fetch_random_images(api_url: str, headers: Dict) -> List[Dict]:
    response = requests.get(
        api_url,
        headers=headers,
        params={"limit": LIMIT_PER_REQUEST},
        timeout=30
    )
    response.raise_for_status()
    return response.json()


def download_image(url: str, filepath: str) -> bool:
    """Скачивает картинку по URL и сохраняет в файл"""
    response = requests.get(url, timeout=30)
    response.raise_for_status()

    with open(filepath, 'wb') as f:
        f.write(response.content)
    return True


def get_file_extension(url: str) -> str:
    ext = url.split('.')[-1].lower().split('?')[0]
    if ext not in ['jpg', 'jpeg', 'png', 'gif', 'webp']:
        ext = 'jpg'
    return ext


def download_images_for_api(api_config: Dict) -> int:
    """
    - Скачивает картинки для конкретного API
    - Возвращает количество скачанных
    """
    images_dir = api_config["images_dir"]
    metadata_file = api_config["metadata_file"]
    api_url = api_config["url"]
    api_key = api_config["key"]
    api_name = api_config["name"]

    Path(images_dir).mkdir(exist_ok=True)

    metadata = load_existing_metadata(metadata_file)
    downloaded_ids = get_downloaded_ids(metadata)

    print(f"\n{'-'*60}")
    print(f"Скачивание {api_name}")
    print(f"{'-'*60}")
    print(f"Уже скачано: {len(downloaded_ids)} картинок")
    print(f"Цель: {TARGET_COUNT} картинок")
    print("-|-" * 40)

    headers = {"x-api-key": api_key}
    failed_count = 0
    initial_count = len(downloaded_ids)

    while len(downloaded_ids) < TARGET_COUNT:
        try:
            images = fetch_random_images(api_url, headers)
            new_in_batch = 0

            for img in images:
                if len(downloaded_ids) >= TARGET_COUNT:
                    break

                img_id = img["id"]

                if img_id in downloaded_ids:
                    continue

                img_url = img["url"]
                ext = get_file_extension(img_url)
                filepath = os.path.join(images_dir, f"{img_id}.{ext}")

                try:
                    download_image(img_url, filepath)

                    metadata["images"].append({
                        "id": img_id,
                        "url": img_url,
                        "width": img.get("width"),
                        "height": img.get("height"),
                        "filename": f"{img_id}.{ext}"
                    })
                    downloaded_ids.add(img_id)
                    new_in_batch += 1

                    print(f"[{len(downloaded_ids):4d}/{TARGET_COUNT}] Скачано: {img_id}.{ext}")

                except requests.RequestException as e:
                    print(f"Ошибка скачивания {img_id}: {e}")
                    continue

            save_metadata(metadata, metadata_file)

            if new_in_batch == 0:
                failed_count += 1
                print(f"Нет новых картинок в пачке ({failed_count}/{MAX_FAILURES})")
            else:
                failed_count = 0

            if failed_count >= MAX_FAILURES:
                print("Слишком много попыток без новых картинок")
                break

            time.sleep(REQUEST_DELAY)

        except requests.RequestException as e:
            print(f"Ошибка API: {e}")
            time.sleep(2)
            failed_count += 1

        except KeyboardInterrupt:
            print("\nПрервано пользователем")
            save_metadata(metadata, metadata_file)
            break

    save_metadata(metadata, metadata_file)

    downloaded_now = len(downloaded_ids) - initial_count
    print("-" * 40)
    print(f"Скачано {api_name}: {downloaded_now} новых, {len(downloaded_ids)} всего")
    print(f"Папка: {images_dir}/")
    print(f"Метаданные: {metadata_file}")

    return len(downloaded_ids)


def parse_args():
    parser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
            Примеры использования:
                python animal_parser.py              # скачать котов и собак
                python animal_parser.py -c -d
                python animal_parser.py --cats       # только коты
                python animal_parser.py --dogs       # только собаки
        """
    )

    parser.add_argument(
        "-c", "--cats",
        action="store_true",
        help="Скачать картинки котов"
    )
    parser.add_argument(
        "-d", "--dogs",
        action="store_true",
        help="Скачать картинки собак"
    )

    return parser.parse_args()


def main():
    args = parse_args()

    download_cats = args.cats or (not args.cats and not args.dogs)
    download_dogs = args.dogs or (not args.cats and not args.dogs)

    results = {}

    if download_cats:
        results["cats"] = download_images_for_api(APIS["cats"])

    if download_dogs:
        results["dogs"] = download_images_for_api(APIS["dogs"])

    print("\n" + "-|-" * 60)
    print("Итог")
    print("-|-" * 60)

    for animal, count in results.items():
        print(f"  {APIS[animal]['name'].capitalize()}: {count} картинок")

    print(f"  Всего: {sum(results.values())} картинок")
    print("-" * 60)


if __name__ == "__main__":
    main()