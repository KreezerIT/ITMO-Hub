#!/bin/bash
if [ ! -f Report.log ]; then
	echo "creating report.log"
	touch Report.log
fi
>report.log
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
    echo "${#arr[*]}" >> Report.log
  fi
done
