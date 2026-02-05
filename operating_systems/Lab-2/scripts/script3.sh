#!/bin/bash
ps -eo pid,lstart --sort=start_time --no-headers | \
tail -n 1 | \
awk '{print $1, $2, $3, $4, $5, $6, $7}'
