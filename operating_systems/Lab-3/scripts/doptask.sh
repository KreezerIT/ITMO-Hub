#!/bin/bash

FIFO="/tmp/guess_fifo"
mkfifo "$FIFO"

cleanup() {
    rm -f "$FIFO"
}
trap cleanup EXIT
compare_count=0

guesser() {
    secret=$((RANDOM % 90000 + 10000))
    echo "Загаданное число: $secret" >&2

    count=0

    for i in {0..4}; do
        digit=${secret:$i:1}

        while read -r try < "$FIFO"; do
            ((compare_count++))
            echo "[Сравнение #$compare_count] Позиция: $i ; Пробуем цифру: $try | Ожидаем: $digit"

            if [[ "$try" == "$digit" ]]; then
                echo "ok" > "$FIFO"
                ((count++))
                break
            else
                echo "no" > "$FIFO"
            fi
        done
    done

    echo "Итог: Угадано $count цифр из 5."
    echo "Всего сравнений сделано: $compare_count"
}

checker() {
    for i in {0..4}; do
        for j in {0..9}; do
            echo "$j" > "$FIFO"
            read -r response < "$FIFO"
            if [[ "$response" == "ok" ]]; then
                echo "Цифра на позиции $i угадана: $j"
                break
            fi
        done
    done
}

guesser &
guesser_pid=$!
checker
wait $guesser_pid
