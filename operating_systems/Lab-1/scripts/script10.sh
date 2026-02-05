#!/bin/bash
man bash | \
tr -cs '[:alpha:]' '\n' | \
grep -E '^.{4,}$' | \
tr '[:upper:]' '[:lower:]' | \
sort | \
uniq -c | \
sort -nr | \
head -n 3

