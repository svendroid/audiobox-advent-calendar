# Audiobox Advent Calendar 🎄

Use your [Creative-Tonie](https://tonies.com/) as a Audio Advent Calendar for your kids.

Get 24 audio tracks, e.g. there are great free kids podcasts out there or create your own, and place them in a folder.
Configure your [config.yaml](src/dist/config.yaml) and afterwards run `./bin/audiobox-advent-calendar sync config.yaml` and all audio files for
today will be synced to your tonie. Make sure to run the sync every day and have a Happy Christmas Time! 🎄

## Setup on Server - Syncing regularly with cronjob

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
        * `crontab -e` and add a new
          line `5 0 1-24 12 * /projects/audiobox-advent-calendar/bin/audiobox-advent-calendar sync /projects/audiobox-advent-calendar/2022-config.yaml`
          which will run the sync "At 00:05 on every day-of-month from 1 through 24 in December."

## Command line Usage

```
Usage: audiobox-advent-calendar sync options_list
Arguments: 
    configPath -> path to your yaml config file. { String }
Options: 
    --dryrun [false] -> dryrun which outputs the actions but wont upload or delete anything 
    --help, -h -> Usage info 
```

