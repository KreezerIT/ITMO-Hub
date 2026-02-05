#!/bin/bash

> mem2.log
> scriptinfo2.log
> 5proc2.log

sleep 5s
pid=$(pgrep -f mem2.bash)

while true; do
  output=$(top -b -n 1)

  echo "$output" | sed '4,5!d' >> mem2.log
  echo "" >> mem2.log

  ps -p $pid -o pid,pcpu,pmem,etime,vsz,rss,cmd >> scriptinfo2.log
  echo "" >> scriptinfo2.log

  echo "$output" | sed '8,12!d' >> 5proc2.log
  echo "" >> 5proc2.log

  if ! ps -p $pid > /dev/null; then
    exit
  fi

  sleep 1s
done


