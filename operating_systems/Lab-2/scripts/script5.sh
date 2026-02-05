#!/bin/bash

output_file=~/lab2/script4.log
> "$output_file"

declare -A ppid_art

for pid_dir in /proc/[0-9]*; do
    pid="${pid_dir##*/}"
    status_file="/proc/$pid/status"
    sched_file="/proc/$pid/sched"

    if [[ -r "$status_file" && -r "$sched_file" ]]; then
        ppid=$(grep "^PPid:" "$status_file" | awk '{print $2}')
        sum_exec_runtime=$(grep "se.sum_exec_runtime" "$sched_file" | awk -F ':' '{gsub(/^[ \t]+/, "", $2); print $2}')
        nr_switches=$(grep "nr_switches" "$sched_file" | awk -F ':' '{gsub(/^[ \t]+/, "", $2); print $2}')

        if [[ "$sum_exec_runtime" =~ ^[0-9.]+$ && "$nr_switches" =~ ^[0-9]+$ ]]; then
            art=$(awk "BEGIN {printf \"%.6f\", $sum_exec_runtime / $nr_switches}")
            ppid_art["$ppid"]+="$art "
            echo "ProcessID=$pid : Parent_ProcessID=$ppid : Average_Running_Time=$art" >> "$output_file"
        fi
    fi
done

sort -t '=' -k4 -n "$output_file" -o "$output_file"

for ppid in "${!ppid_art[@]}"; do
    arts=(${ppid_art[$ppid]})
    sum=0
    count=${#arts[@]}
    for art in "${arts[@]}"; do
        sum=$(awk "BEGIN {print $sum + $art}")
    done
    avg_art=$(awk "BEGIN {printf \"%.6f\", $sum / $count}")
    echo "Average_Running_Children_of_ParentID=$ppid is $avg_art" >> "$output_file"
done
