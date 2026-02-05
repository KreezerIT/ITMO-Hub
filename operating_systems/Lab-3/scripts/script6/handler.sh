#!/bin/bash

value=1
running=true
operation="add"

handle_usr1() {
    operation="add"
}

handle_usr2() {
    operation="mul"
}

handle_term() {
    echo "обработчик завершает работу по сигналу SIGTERM от другого процесса"
    running=false
}

trap 'handle_usr1' USR1
trap 'handle_usr2' USR2
trap 'handle_term' TERM

echo "обработчик запущен. PID: $$"
echo "стартовое значение: $value"

while $running; do
    sleep 1
    if [[ "$operation" == "add" ]]; then
        value=$((value + 2))
        echo "'+', значение: $value"
    else
        value=$((value * 2))
        echo "'*', значение: $value"
    fi
done

