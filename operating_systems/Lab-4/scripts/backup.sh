#!/bin/bash

HOME_DIR="$HOME"
SOURCE_DIR="$HOME_DIR/source"
REPORT_FILE="$HOME_DIR/backup-report"

CURRENT_DATE=$(date +%F)
BACKUP_DIR_NAME="Backup-$CURRENT_DATE"
BACKUP_DIR="$HOME_DIR/$BACKUP_DIR_NAME"

if [[ ! -d "$SOURCE_DIR" ]]; then
    echo "Error: директория $SOURCE_DIR не существует" >&2
    exit 1
fi

EXISTING_BACKUP=""
for dir in "$HOME_DIR"/Backup-*; do
    [[ ! -d "$dir" ]] && continue
    dir_date=$(basename "$dir" | cut -d '-' -f2-)
    if [[ "$dir_date" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]; then
    timeDiff=$(( $(date -d "$CURRENT_DATE" +%s) - $(date -d "$dir_date" +%s) ))
    if (( timeDiff < 604800 )); then
        EXISTING_BACKUP="$dir"
        break
    fi
fi

done

log_new_backup() {
    echo "Создан новый каталог резервного копирования: $BACKUP_DIR_NAME ($CURRENT_DATE)" >> "$REPORT_FILE"
    for file in "$SOURCE_DIR"/*; do
        [[ -f "$file" ]] || continue
        filename=$(basename "$file")
        echo "$filename" >> "$REPORT_FILE"
    done
}

log_updated_backup() {
    echo "Внесены изменения в каталог резервного копирования: $(basename "$EXISTING_BACKUP") ($CURRENT_DATE)" >> "$REPORT_FILE"
}

if [[ -z "$EXISTING_BACKUP" ]]; then
    mkdir "$BACKUP_DIR" 2>/dev/null
    if [[ $? -ne 0 ]]; then
        echo "Error: не удалось создать каталог $BACKUP_DIR" >&2
        exit 1
    fi
    cp -r --preserve=all "$SOURCE_DIR"/* "$BACKUP_DIR"/ 2>/dev/null
    if [[ $? -ne 0 ]]; then
        echo "Error при копировании файлов в $BACKUP_DIR" >&2
        exit 1
    fi
    log_new_backup
else
    updated_files=()
    renamed_files=()
    for src_file in "$SOURCE_DIR"/*; do
        [[ -f "$src_file" ]] || continue
        filename=$(basename "$src_file")
        dest_file="$EXISTING_BACKUP/$filename"
        if [[ ! -f "$dest_file" ]]; then
            cp --preserve=all "$src_file" "$dest_file" 2>/dev/null
            if [[ $? -eq 0 ]]; then
                updated_files+=("$filename")
            fi
        else
            src_size=$(stat -c %s "$src_file")
            dest_size=$(stat -c %s "$dest_file")
            if [[ "$src_size" -ne "$dest_size" ]]; then
                old_version="$EXISTING_BACKUP/$filename.$CURRENT_DATE"
                mv "$dest_file" "$old_version" 2>/dev/null
                if [[ $? -ne 0 ]]; then
                    echo "Error при переименовании $dest_file в $old_version" >&2
                    continue
                fi
                cp --preserve=all "$src_file" "$dest_file" 2>/dev/null
                if [[ $? -eq 0 ]]; then
                    renamed_files+=("$filename $filename.$CURRENT_DATE")
                fi
            fi
        fi
    done

    if [[ ${#updated_files[@]} -gt 0 || ${#renamed_files[@]} -gt 0 ]]; then
        log_updated_backup
        for f in "${updated_files[@]}"; do
            echo "$f" >> "$REPORT_FILE"
        done
        for pair in "${renamed_files[@]}"; do
            echo "$pair" >> "$REPORT_FILE"
        done
    fi
fi

