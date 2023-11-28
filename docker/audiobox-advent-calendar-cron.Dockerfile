FROM ghcr.io/svendroid/audiobox-advent-calendar-standalone:latest

ADD ./docker/entrypoint.sh .
RUN chmod 755 /entrypoint.sh

# Create the log file to be able to run tail in script
RUN touch /var/log/cron.log

# start cron
CMD ["/entrypoint.sh"]

ENTRYPOINT []