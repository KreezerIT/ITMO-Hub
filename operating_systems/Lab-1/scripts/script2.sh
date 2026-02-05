#!/bin/bash
result=""
while true; do
	read -p "Enter string to save or 'q' to quit: " input

	if [ "$input" == "q" ]; then
		break
	fi

	result="$result$input "
done

echo "Entered lines: $result"
