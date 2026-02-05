#!/bin/bash
a=$1; b=$2; c=$3
sum=$((a+b+c))

if ((sum - a < sum - b && sum - a < sum - c)); then
	echo "Max = $a"

elif ((sum - b < sum - a && sum - b < sum - c)); then
	echo "Max = $b"

else
	echo "Max = $c"
fi
