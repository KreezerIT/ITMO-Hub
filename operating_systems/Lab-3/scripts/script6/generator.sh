#!/bin/bash

if [[ -z "$1" ]]; then
    echo "использование: $0"
    exit 1
fi

PROCESS_PID=$1

echo "Генератор запущен. Команды:"
echo "'+' — послать SIGUSR1 (прибавить 2)"
echo "'*' — послать SIGUSR2 (умножить на 2)"
echo "'TERM' — завершить работу обработчика"

while true; do
    read -r input
    case "$input" in
        "+")
            kill -USR1 "$PROCESS_PID"
            ;;
        "*")
            kill -USR2 "$PROCESS_PID"
            ;;
        "TERM")
            kill -TERM "$PROCESS_PID"
            echo "генератор завершает работу"
            exit 0
            ;;
        *)
            ;;
    esac
done

