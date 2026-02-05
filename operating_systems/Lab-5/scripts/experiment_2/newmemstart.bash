#!/bin/bash
k=$1
for ((i=0;i<k;i++))
do
	./newmem.bash 11300000 &
	sleep 1s
done
wait
