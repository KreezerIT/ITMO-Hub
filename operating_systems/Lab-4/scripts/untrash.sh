#!/bin/bash

TRASH_DIR="$HOME/.trash"
TRASH_LOG="$HOME/.trash.log"

if [ "$#" -ne 1 ]; then
    echo "Error: ввод должен содержать один параметр – имя файла для восстановления" >&2
    exit 1
fi

FILENAME="$1"

if [ ! -d "$TRASH_DIR" ] || [ ! -f "$TRASH_LOG" ]; then
    echo "Error: корзина или лог-файл не найдены" >&2
    exit 2
fi

MATCHES=$(grep ".*\/$FILENAME -> [0-9]\+$" "$TRASH_LOG")

if [ -z "$MATCHES" ]; then
    echo "Файл '$FILENAME' не найден в корзине" >&2
    exit 3
fi

echo "Найдено совпадение для '$FILENAME':"
echo "$MATCHES"

while IFS= read -r LINE; do
    ORIG_PATH=$(echo "$LINE" | cut -d' ' -f1)
    LINK_NAME=$(echo "$LINE" | awk '{print $3}')
    LINK_PATH="$TRASH_DIR/$LINK_NAME"

    echo "Найден файл: $ORIG_PATH"

    read -p "Восстановить его? [y/n]: " CONFIRM </dev/tty


    CONFIRM=$(echo "$CONFIRM" | tr -d '[:space:]')

    if [[ "$CONFIRM" =~ ^[Yy]$ ]]; then
        RESTORE_DIR=$(dirname "$ORIG_PATH")

        if [ ! -d "$RESTORE_DIR" ]; then
            echo "Каталог '$RESTORE_DIR' не существует. Восстанавливаю файл в домашний каталог"
            RESTORE_PATH="$HOME/$FILENAME"
        else
            RESTORE_PATH="$ORIG_PATH"
        fi

        if [ -e "$RESTORE_PATH" ]; then
            echo "Файл '$RESTORE_PATH' уже существует"
            read -p "Введите новое имя для восстановления: " NEW_NAME
            RESTORE_PATH="$(dirname "$RESTORE_PATH")/$NEW_NAME"
        fi

        ln "$LINK_PATH" "$RESTORE_PATH" 2>/dev/null
        if [ $? -ne 0 ]; then
            echo "Error: не удалось создать ссылку '$RESTORE_PATH'" >&2
            continue
        fi

        rm "$LINK_PATH"
        if [ $? -ne 0 ]; then
            echo "Warning: ссылка восстановлена, но не удалось удалить из корзины" >&2
        fi

        grep -vFx "$LINE" "$TRASH_LOG" > "$TRASH_LOG.tmp" && mv "$TRASH_LOG.tmp" "$TRASH_LOG"

        echo "Файл успешно восстановлен в: $RESTORE_PATH"
    else
        echo "Восстановление файла '$ORIG_PATH' отменено"
    fi
done < <(echo "$MATCHES")

