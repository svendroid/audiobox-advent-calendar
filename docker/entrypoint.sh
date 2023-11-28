#!/bin/sh

# configure cronjob
crontab config/crontab.txt

# start cron
/usr/sbin/crond -l 8

# output logfile of cronjob
tail -f /var/log/cron.log
