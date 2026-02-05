#!/bin/bash
file1="/var/log/anaconda/syslog"
file2="/var/log/installer/syslog"
outputFile="info.log"

if [ -f "$file1" ]; then
    awk '$2 == "INFO" {print}' "$file1" > "$outputFile"
elif [ -f "$file2" ]; then
    awk '$2 == "INFO" {print}' "$file2" > "$outputFile"
else
    echo "File not found" > "$outputFile"
fi
