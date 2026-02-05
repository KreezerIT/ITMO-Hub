#!/bin/bash

logFile="/var/log/anaconda/X.log"
if [ ! -f "$logFile" ]; then
  logFile="/var/log/installer/X.0.log"
fi

if [ ! -f "$logFile" ]; then
  echo "File not found"
  exit 1
fi

grep -E 'Warning|Information' "$logFile" | \
sed -e 's/\[.*\] Warning:/Warning:/' -e 's/\[.*\] Information:/Information:/' | \
sort > full.log
cat full.log

