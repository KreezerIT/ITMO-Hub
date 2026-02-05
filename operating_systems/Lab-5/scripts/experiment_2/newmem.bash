#!/bin/bash
arr=()
while true
do
  for j in {1..10}
  do
    arr+=($j)
  	if [ ${#arr[*]} -gt $1 ]; then
    	exit
  	fi
	done
done
