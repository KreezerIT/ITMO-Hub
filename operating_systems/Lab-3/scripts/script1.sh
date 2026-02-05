#!/bin/bash

log_success() {
  echo "catalog test was created successfully" >> ~/report
  touch ~/test/$(date +"%Y-%m-%d_%H-%M-%S")
}

log_ping_fail() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') ping error to www.net_nikogo.ru" >> ~/report
}

mkdir ~/test 2>/dev/null && log_success
ping -c 1 www.net_nikogo.ru >/dev/null 2>&1 || log_ping_fail
