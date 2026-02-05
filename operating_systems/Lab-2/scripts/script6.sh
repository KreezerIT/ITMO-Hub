#!/bin/bash
max_mem=0
pid_max_mem=0

for pid in /proc/[0-9]*; do

    if [[ -f "$pid/status" ]]; then

        mem=$(grep VmRSS "$pid/status" | awk '{print $2}')

        if ((mem > max_mem)); then
            max_mem=$mem
            pid_max_mem=$(basename "$pid")
        fi
    fi
done

echo "Most used memory process:"
echo "PID: $pid_max_mem"
echo "Memory usage: $max_mem KB"
echo -e "\nResult of 'top' command:"
top -b -n 1 -o %MEM | head -n 12
