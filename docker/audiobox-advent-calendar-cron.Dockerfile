FROM audiobox-advent-calendar-standalone

ADD ./docker/entrypoint.sh .
RUN chmod 755 /entrypoint.sh

# Create the log file to be able to run tail in script
RUN touch /var/log/cron.log

# start cron
CMD ["/entrypoint.sh"]

ENTRYPOINT []