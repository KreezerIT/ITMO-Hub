#!/bin/bash

PIPE="script5_pipe"
mode="add"
result=1
echo "Обработчик запущен. Жду ввод"

while true; do
    if read -r line < "$PIPE"; then
        case "$line" in
            "+")
                mode="add"
                ;;
            "*")
                mode="mul"
                ;;
            "q")
                echo "завершение. Результат: $result"
                break
                ;;
            ''|*[!0-9-]*)
                    echo "неверный ввод — '$line'"
                break
                ;;
            *)
                if [[ "$line" =~ ^-?[0-9]+$ ]]; then
                    number=$line
                    if [[ "$mode" == "add" ]]; then
                        result=$((result + number))
                        echo "'+' к: $number, результат: $result"
                    else
                        result=$((result * number))
                        echo "'*' на: $number, результат: $result"
                    fi
                else
                    echo "неверный ввод — '$line'"
                    break
                fi
                ;;
        esac
    fi
done

rm -f "$PIPE"

