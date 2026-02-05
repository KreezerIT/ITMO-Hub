#!/bin/bash

> mem.log
> scriptinfo.log
> 5proc.log

sleep 5s
pid=$(pgrep -f mem.bash)

while true; do
  output=$(top -b -n 1)

  echo "$output" | sed '4,5!d' >> mem.log
  echo "" >> mem.log

  ps -p $pid -o pid,pcpu,pmem,etime,vsz,rss,cmd >> scriptinfo.log
  echo "" >> scriptinfo.log

  echo "$output" | sed '8,12!d' >> 5proc.log
  echo "" >> 5proc.log

  if ! ps -p $pid > /dev/null; then
    exit
  fi

  sleep 1s
done


