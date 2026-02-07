import os
import pandas as pd
from yandex_music import Client
import time
import json
from dotenv import load_dotenv

load_dotenv()

TOKEN = os.getenv('YANDEX_MUSIC_TOKEN')
DATASET_FILENAME = os.getenv('DATASET_FILENAME')

if not TOKEN:
    print("Токен не найден")
    exit(1)

if not DATASET_FILENAME:
    print("Датасет не найден")
    exit(1)

try:
    df = pd.read_csv(DATASET_FILENAME)
except FileNotFoundError:
    print(f"Файл {DATASET_FILENAME} не найден")
    exit(1)

client = Client(TOKEN).init()

base_dir = os.getenv('DOWNLOAD_PATH', 'yandex_music_dataset')
download_path = f'{base_dir}/audio_files'
os.makedirs(download_path, exist_ok=True)

downloaded_info = []
failed_tracks = []

total_tracks = len(df)
success_count = 0

print(f"Скачивание {total_tracks} треков:")
print(f"Файлы будут сохраняться в: {download_path}")
print("-" * 65)

# Словарь для быстрого поиска жанра по ID
genre_by_id = dict(zip(df['id'], df['genre']))

for idx, track_id in enumerate(df['id'], 1):
    try:
        print(f"[{idx}/{total_tracks}] ID: {track_id}", end=" ")

        # Объект трека
        track = client.tracks([track_id])[0]

        artist_name = track.artists[0].name if track.artists else "Unknown"
        track_title = track.title

        genre = genre_by_id.get(track_id, "unknown")

        # Безопасное имя файла
        safe_artist = "".join([c for c in artist_name if c not in '\\/*?:"<>|'])
        safe_title = "".join([c for c in track_title if c not in '\\/*?:"<>|'])
        safe_filename = f"{safe_artist} - {safe_title}.mp3"

        full_path = f"{download_path}/{safe_filename}"
        track.download(full_path, codec='mp3', bitrate_in_kbps=192)

        downloaded_info.append({
            'original_id': track_id,
            'filename': safe_filename,
            'file_path': full_path,
            'artist': artist_name,
            'title': track_title,
            'genre': genre,
            'downloaded_success': True,
        })

        success_count += 1
        print(f"~SUCCESS~ ({genre})")
        time.sleep(float(os.getenv('DOWNLOAD_SLEEP_TIME', 1.5)))

    except Exception as e:
        print(f"Ошибка: {type(e).__name__}")
        failed_tracks.append({
            'id': track_id,
            'error': str(e)
        })

print("-" * 65)
print(f"Успешно: {success_count} / {total_tracks}")
print(f"Не удалось: {len(failed_tracks)}")

# Сохранение в JSON
metadata_path = f'{base_dir}/metadata.json'
with open(metadata_path, 'w', encoding='utf-8') as f:
    json.dump({
        'total_tracks': total_tracks,
        'successful_downloads': success_count,
        'failed_downloads': len(failed_tracks),
        'tracks': downloaded_info,
        'failed': failed_tracks
    }, f, ensure_ascii=False, indent=2)

print(f"\nМетаданные сохранены в: {metadata_path}")

# Сохранение в CSV
df_metadata = pd.DataFrame(downloaded_info)
csv_path = f'{base_dir}/track_metadata.csv'
df_metadata.to_csv(csv_path, index=False, encoding='utf-8-sig')
print(f"Таблица метаданных сохранена в: {csv_path}")
