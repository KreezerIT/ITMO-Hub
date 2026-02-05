#!/bin/bash

touch memory_temp
touch memory_temp2

for var in $(ps -A | sed '1d' | awk '{print $1}')
do
    if [ -f "/proc/$var/io" ]
    then
        memory=$(sudo grep "read_bytes:" "/proc/$var/io" | awk '{print $2}')
        echo "$var $memory" >> memory_temp
    fi
done

sleep 60s

for var in $(ps -A | sed '1d' | awk '{print $1}')
do
    if [ -f "/proc/$var/io" ]
    then
        memory=$(sudo grep "read_bytes:" "/proc/$var/io" | awk '{print $2}')
        old_memory=0

        if [[ $(grep "$var" memory_temp | wc -l) -ne 0 ]]
        then
            old_memory=$(grep "$var" memory_temp | awk '{print $2}')
        fi

        diff=$(echo "$memory - $old_memory" | bc -l)
        cmd=$(cat "/proc/$var/cmdline" | tr '\0' ' ')
        echo "$var : $cmd : $diff" >> memory_temp2
    fi
done

sort -nrk5 memory_temp2 | head -n 3

rm memory_temp
rm memory_temp2
