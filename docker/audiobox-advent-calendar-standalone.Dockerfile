FROM eclipse-temurin:17-jdk-alpine

# using ADD instead of COPY to automatically untar into a folder
ADD build/distributions/audiobox-advent-calendar-*.tar .
# Remove version number
RUN mv audiobox-advent-calendar-* audiobox-advent-calendar

ENTRYPOINT ["audiobox-advent-calendar/bin/audiobox-advent-calendar"]