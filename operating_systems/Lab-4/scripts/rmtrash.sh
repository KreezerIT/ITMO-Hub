#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Error: ввод должен содержать один параметр – имя файла" >&2
    exit 1
fi

FILE="$1"
if [ ! -e "$FILE" ]; then
    echo "Error: файл '$FILE' не существует" >&2
    exit 2
fi

if [ ! -f "$FILE" ]; then
    echo "Error: '$FILE' не является обычным файлом" >&2
    exit 3
fi

TRASH_DIR="$HOME/.trash"
TRASH_LOG="$HOME/.trash.log"
if [ ! -d "$TRASH_DIR" ]; then
    mkdir -p "$TRASH_DIR" || {
        echo "Error: не удалось создать директорию '$TRASH_DIR'" >&2
        exit 4
    }
fi

LINK_NAME=$(find "$TRASH_DIR" -type f -printf "%f\n" 2>/dev/null | grep -E '^[0-9]+$' | sort -n | tail -n 1)
if [[ -z "$LINK_NAME" ]]; then
    LINK_ID=1
else
    LINK_ID=$((LINK_NAME + 1))
fi

LINK_PATH="$TRASH_DIR/$LINK_ID"

ABS_PATH="$(readlink -f -- "$FILE")"

ln -- "$FILE" "$LINK_PATH" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "Error: не удалось создать жесткую ссылку '$LINK_PATH' для '$FILE'" >&2
    exit 5
fi

rm -- "$FILE" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "Error: не удалось удалить файл '$FILE'" >&2
    rm -- "$LINK_PATH" 2>/dev/null
    exit 6
fi

echo "$ABS_PATH -> $LINK_ID" >> "$TRASH_LOG" 2>/dev/null || {
    echo "Error: не удалось записать лог '$TRASH_LOG'" >&2
    exit 7
}

echo "Успех: файл '$FILE' перемещен в корзину как '$LINK_ID'"
