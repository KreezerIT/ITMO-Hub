#!/bin/bash

find /etc -type f -exec grep -aoE \
'@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}'\
 {} + \
| sort -u | \
tr '\n' ',' \
> emails.lst

