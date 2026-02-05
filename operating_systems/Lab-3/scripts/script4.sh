#!/bin/bash

infinite_multiply() {
  while true; do
    echo $((1 * 2)) > /dev/null
  done
}

infinite_multiply &
PID1=$!
echo "процесс 1 PID: $PID1"

infinite_multiply &
PID2=$!
echo "процесс 2 PID: $PID2"

infinite_multiply &
PID3=$!
echo "процесс 3 PID: $PID3"

cpulimit -p $PID1 -l 10 &

sleep 2
echo "команда top. 'q' для выхода"
top

echo "killing $PID3"
kill $PID3
sleep 3

