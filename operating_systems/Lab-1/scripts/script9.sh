#!/bin/bash
find /var/log/ -type f -name "*.log" \
-exec awk 'END {print NR}' {} + | \
awk '{sum+=$1} END {print sum}'

