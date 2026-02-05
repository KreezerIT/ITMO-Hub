#!/bin/bash

TARGET_SCRIPT="/home/vboxuser/lab3/script1.sh"
(sleep 120 && bash "$TARGET_SCRIPT") &
tail -f ~/report
