# Audiobox Advent Calendar 🎄 (use it with your Toniebox)

Use your [Creative-Tonie](https://tonies.com/) of your Toniebox as a Audio Advent Calendar for your kids.

Get 24 audio tracks, e.g. there are great free kids podcasts out there or create your own, and place them in a folder.
Configure your [config.yaml](src/dist/config.yaml) and afterwards run `./bin/audiobox-advent-calendar sync config.yaml` and all audio files for
today will be synced to your tonie. Make sure to run the sync every day and have a Happy Christmas Time! 🎄

## Run with standalone docker image

* Configure your [docker/config/config.yaml](docker/config/config.yaml)
* Place your audio files in a folder e.g. `docker/audioFiles`
* Run `docker run audiobox-advent-calendar-standalone:latest sync -h` to output available options
* Run `docker run -v ${PWD}/docker/config.yaml:/config.yaml -v ${PWD}/docker/audioFiles:/audioFiles audiobox-advent-calendar-standalone:latest sync config.yaml` 
  * will sync audio files from `docker/audioFiles` for today to the tonie. e.g. if today is december 2nd, two files will be synced.
  * You would need to run this command every day.

## Setup on Server - Sync automatically with docker cron image

* Edit [docker/config/config.yaml](docker/config/config.yaml)
* Place your audio files in `docker/audioFiles`
* Switch to docker dir `cd docker`
* Run `docker-compose up` it will sync files at 00:05 on every day-of-month from 1 through 24 in December. 
* Read & Edit [crontab.txt](docker/config/crontab.txt) to adjust sync interval.

## Setup on Server - Sync automatically on linux with cronjob

* On Client:
    * `./gradlew assembleDist` - Create distributions
    * `rsync -a ./build/distributions/audiobox-advent-calendar-0.1.tar user@yourserver:/projects/` - Upload program to
    * `rsync -a -v --stats --progress "/tonies/adventstories/" user@yourserver:/projects/audiobox-advent-calendar/2022-stories` -
      Upload audio files to server
* On Server: 
    * `mkdir audiobox-advent-calendar`
    * `tar -xvf audiobox-advent-calendar-0.1.tar -C audiobox-advent-calendar --strip-components=1` - untar contents to project directory
    * Create your own config
      * `cd audiobox-advent-calendar` 
      * `cp config.yaml 2022-config.yaml`
      * edit `2022-config.yaml`
    * Run sync
        * make sure java is installed - e.g. `apt install openjdk-11-jre-headless`
        * run `./bin/audiobox-advent-calendar sync ./2022-config.yaml --dryrun` to check your config
        * run without dryrun to start sync
    * Setup cronjob:
        * `crontab -e`
        * add a new line `5 0 1-24 12 * /projects/audiobox-advent-calendar/bin/audiobox-advent-calendar sync /projects/audiobox-advent-calendar/2022-config.yaml 2>&1 | logger -t adventcalendarsync`
          which will run the sync "At 00:05 on every day-of-month from 1 through 24 in December."
        * `grep 'adventcalendarsync' /var/log/syslog` to get logs

## Command line Usage

```
Usage: audiobox-advent-calendar sync options_list
Arguments: 
    configPath -> path to your yaml config file. { String }
Options: 
    --dryrun [false] -> dryrun which outputs the actions but wont upload or delete anything 
    --help, -h -> Usage info 
```



## Development notes

### Docker
* Build image locally
  * `docker build -f docker/audiobox-advent-calendar-standalone.Dockerfile -t audiobox-advent-calendar-standalone:latest .`