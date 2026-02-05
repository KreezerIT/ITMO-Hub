#!/bin/bash
PS3="Choose an option: "

select opt in "nano" "vi" "firefox" "exit"; do
    case $REPLY in
        1) nano ;;
        2) vi ;;
        3) firefox ;;
        4) break ;;
        *) echo "Wrong input" ;;
    esac
done
