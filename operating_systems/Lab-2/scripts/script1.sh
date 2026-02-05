#!/bin/bash
output=~/lab2/script1.log

ps -u vboxuser -o pid=,comm= | \
tee >(awk 'END {print NR}' > "$output") | \
awk '{print $1 ":" $2}' \
>> "$output"

