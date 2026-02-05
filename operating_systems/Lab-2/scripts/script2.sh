#!/bin/bash
output=~/lab2/script2.log
ps -eo pid,cmd | \
grep '^ *[0-9]' | \
grep ' /sbin/' | \
awk '{print $1}' \
> "$output"
