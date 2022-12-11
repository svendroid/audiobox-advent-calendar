import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.cli.*
import java.io.File
import java.time.LocalDate

@ExperimentalCli
fun main(args: Array<String>) {
    val parser = ArgParser("audiobox-advent-calendar")

    class Sync : Subcommand(
        "sync",
        "Syncs all audio files which are in the adventcalendar till today and uploads them to a tonie.\n" +
                "The first day the first element in the folder, the second day the second element, ... - till it is christmas\n" +
                "If the tonie is full (only 90min are permitted) the first days are deleted to make space and only the most recent days\n" +
                "which fit into the 90 min will remain. \n" +
                "Happy Christmas Time!"
    ) {
        val configPath by argument(
            type = ArgType.String,
            description = "path to your yaml config file."
        )
        val dryrun by option(
            ArgType.Boolean,
            fullName = "dryrun",
            description = "dryrun which outputs the actions but wont upload or delete anything"
        ).default(false)

        override fun execute() {

            val mapper = YAMLMapper()
                .registerKotlinModule()
                .registerModule(JavaTimeModule())
            val config = mapper.readValue(File(configPath), Configuration::class.java)
            println(config)

            val app = AdventCalendarApp(
                username = config.username,
                password = config.password,
                folderPath = config.folder,
                tonieName = config.tonieName,
                householdName = config.householdName,
                startDate = config.startDate,
                endDate = config.endDate
            )
            app.sync(dryrun = dryrun)
        }
    }

    val syncCommand = Sync()
    parser.subcommands(syncCommand)
    parser.parse(args)
}

data class Configuration(
    val username: String,
    val password: String,
    val folder: String,
    val tonieName: String,
    val householdName: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?
)