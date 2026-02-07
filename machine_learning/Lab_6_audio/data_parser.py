import os
import pandas as pd
from yandex_music import Client
import time
from dotenv import load_dotenv

load_dotenv()
TOKEN = os.getenv('YANDEX_MUSIC_TOKEN')
client = Client(TOKEN).init()

genres_to_collect = os.getenv('GENRES_TO_COLLECT', '').split(',')
all_tracks = []
seen_track_ids = set()

TARGET_TRACKS_AMOUNT_PER_GENRE=os.getenv('TARGET_TRACKS_AMOUNT_PER_GENRE', 50)

def get_tracks_from_search(genre, num_tracks):
    """Сбор информации о треках через прямой поиск"""
    tracks = []
    try:
        for page in range(0, os.getenv('MAX_PAGE_SEARCH', 2)):
            search = client.search(genre, type_='track', page=page)
            time.sleep(float(os.getenv('PARSE_SLEEP_TIME', 0.7)))

            if search and search.tracks and search.tracks.results:
                for track in search.tracks.results[:os.getenv('MAX_TRACKS_AMOUNT_PER_PAGE', 15)]:
                    if track.id not in seen_track_ids:

                        # Полная версия трека
                        full_track = client.tracks([track.id])[0]
                        artist_name = full_track.artists[0].name if full_track.artists else "Неизвестно"

                        tracks.append({
                            'id': track.id,
                            'title': track.title,
                            'artist': artist_name,
                            'genre': genre,
                            'source': 'track_search'
                        })
                        seen_track_ids.add(track.id)

                        if len(tracks) >= num_tracks:
                            return tracks
    except Exception as e:
        print(f"  Ошибка при поиске треков: {e}")
    return tracks


def get_tracks_from_artists(genre, num_tracks):
    """Сбор информации о треках через поиск исполнителей жанра"""
    tracks = []
    try:
        artist_search = client.search(genre, type_='artist', page=0)
        time.sleep(float(os.getenv('PARSE_SLEEP_TIME', 0.7)))

        if artist_search and artist_search.artists:
            artist_ids = [a.id for a in artist_search.artists.results[:os.getenv('AMOUNT_OF_ARTISTS_TO_SEARCH_FOR_GENRE', 5)]]

            for artist_id in artist_ids:
                artist_tracks = client.artists_tracks(artist_id, page_size=os.getenv('PAGE_SIZE_FOR_ARTIST_SEARCH_TRACKS', 15))
                if artist_tracks and artist_tracks.tracks:
                    for track in artist_tracks.tracks[:os.getenv('AMOUNT_OF_ARTISTS_TRACKS_TO_SEARCH_FOR_GENRE', 10)]:
                        if track.id not in seen_track_ids:
                            artist_name = track.artists[0].name if track.artists else "Неизвестно"
                            tracks.append({
                                'id': track.id,
                                'title': track.title,
                                'artist': artist_name,
                                'genre': genre,
                                'source': 'artist_tracks'
                            })
                            seen_track_ids.add(track.id)

                            if len(tracks) >= num_tracks:
                                return tracks
                time.sleep(float(os.getenv('PARSE_SLEEP_TIME', 0.7)))
    except Exception as e:
        print(f"  Ошибка при работе с исполнителями: {e}")
    return tracks


print("Сбор информации о треках:")
print("-" * 65)

for genre in genres_to_collect:
    print(f"\n~~~ Сбор для жанра '{genre.upper()}' ~~~")
    genre_tracks = []
    methods = [
        ('Поиск треков', get_tracks_from_search),
        ('Исполнители', get_tracks_from_artists)
    ]

    for method_name, method_func in methods:
        print(f"\n  Метод: {method_name}")
        new_tracks = method_func(genre, num_tracks=os.getenv('MAX_TRACKS_PER_GENRE_SEARCH_METHOD', 20))
        genre_tracks.extend(new_tracks)
        print(f"  Найдено новых треков: {len(new_tracks)}")
        time.sleep(1)

        if len(genre_tracks) >= TARGET_TRACKS_AMOUNT_PER_GENRE:
            break

    # Альтернативный поиск треков при недостатке
    if len(genre_tracks) < TARGET_TRACKS_AMOUNT_PER_GENRE:
        print(f"\n  ~Дополнительный поиск треков~")
        additional_search = client.search(f"{genre} 2023", type_='track', page=0)
        if additional_search and additional_search.tracks:
            for track in additional_search.tracks.results[:os.getenv('ADDITIONAL_SEARCH_TRACKS_LIMIT', 30)]:
                if track.id not in seen_track_ids:
                    artist_name = track.artists[0].name if track.artists else "Неизвестно"
                    genre_tracks.append({
                        'id': track.id,
                        'title': track.title,
                        'artist': artist_name,
                        'genre': genre,
                        'source': 'additional_search'
                    })
                    seen_track_ids.add(track.id)

    # Добавляем треки в общий список
    all_tracks.extend(genre_tracks[:TARGET_TRACKS_AMOUNT_PER_GENRE])
    print(f"  Итого для жанра '{genre}': {len(genre_tracks[:TARGET_TRACKS_AMOUNT_PER_GENRE])} треков")

print("\n" + "-" * 65)
print("Результаты сбора информации о треках:")
print("-" * 65)

if all_tracks:
    df = pd.DataFrame(all_tracks)

    total_tracks = len(df)
    genre_stats = df['genre'].value_counts()
    source_stats = df['source'].value_counts()

    print(f"Всего собрано уникальных треков: {total_tracks}")
    print("\nРаспределение по жанрам:")
    for genre, count in genre_stats.items():
        print(f"  {genre}: {count} треков")

    print("\nИсточники данных:")
    for source, count in source_stats.items():
        print(f"  {source}: {count} треков")

    # Сохранение
    filename = os.getenv('DATASET_FILENAME', 'music_dataset.csv')
    df.to_csv(filename, index=False, encoding='utf-8-sig')
    print(f"\nДанные сохранены в: {filename}")
else:
    print("Ошибка при сборе треков")
