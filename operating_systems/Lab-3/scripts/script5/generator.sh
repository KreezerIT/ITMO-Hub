#!/bin/bash

PIPE="script5_pipe"

if [[ ! -p $PIPE ]]; then
    mkfifo "$PIPE"
fi

echo "Генератор работает. Команды:"
echo "'+' - сложение"
echo "'*' - умножение"
echo "'q' - выход"


while true; do
    read -r input
    echo "$input" > "$PIPE"
    if [[ "$input" == "q" ]]; then
        break
    fi
done
