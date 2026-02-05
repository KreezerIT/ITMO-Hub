#!/bin/bash
if [ ! -f report2.log ]; then
	echo "creating report2.log"
	touch report2.log
fi
>report2.log
arr=()
i=0
while true
do
  for j in {1..10}
  do
    arr+=($i)
	done
  ((i++))
  if (( $i % 100000 == 0 )); then
    echo "${#arr[*]}" >> report2.log
  fi
done
