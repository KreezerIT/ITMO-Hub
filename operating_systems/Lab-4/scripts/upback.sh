#!/bin/bash

HOME_DIR="$HOME"
RESTORE_DIR="$HOME_DIR/restore"

if [[ ! -d "$RESTORE_DIR" ]]; then
    echo "Error: директория $RESTORE_DIR не существует, создаю" >&2
    mkdir -p "$RESTORE_DIR"
    if [[ $? -ne 0 ]]; then
        echo "Error: не удалось создать директорию $RESTORE_DIR" >&2
        exit 1
    fi
fi

LATEST_BACKUP=""
for dir in "$HOME_DIR"/Backup-*; do
    [[ ! -d "$dir" ]] && continue
    dir_date=$(basename "$dir" | cut -d '-' -f2-)
    if [[ "$dir_date" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        if [[ -z "$LATEST_BACKUP" || $(date -d "$dir_date" +%s) -gt $(date -d "$(basename "$LATEST_BACKUP" | cut -d '-' -f2-) " +%s) ]]; then
            LATEST_BACKUP="$dir"
        fi
    fi
done

if [[ -z "$LATEST_BACKUP" ]]; then
    echo "Error: не найдено актуальных каталогов резервного копирования" >&2
    exit 1
fi

for file in "$LATEST_BACKUP"/*; do
    [[ -f "$file" ]] || continue
    filename=$(basename "$file")
    
    if [[ "$filename" =~ \.[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
        continue
    fi
    
    cp --preserve=all "$file" "$RESTORE_DIR"
    if [[ $? -ne 0 ]]; then
        echo "Error при копировании файла $file в $RESTORE_DIR" >&2
        exit 1
    fi
done
echo "Успех"

