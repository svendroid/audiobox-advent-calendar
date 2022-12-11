import mu.KotlinLogging
import rocks.voss.toniebox.TonieHandler
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class AdventCalendarApp(
    val username: String,
    val password: String,
    val folderPath: String,
    val tonieName: String,
    val householdName: String? = null,
    val startDate: LocalDate?,
    val endDate: LocalDate?
) {

    fun sync(dryrun: Boolean = false) {
        logger.info { "start sync ${if (dryrun) "- dryrun" else ""}" }
        val fileManager = AudioFilesManager(
            File(folderPath),
            startDate ?: LocalDate.now().withMonth(12).withDayOfMonth(1),
            endDate ?: LocalDate.now().withMonth(12).withDayOfMonth(24)
        )

        val tonieHandler = TonieHandler()
        tonieHandler.login(username, password)

        val households = tonieHandler.households
        val household = if (householdName != null) {
            households.find { it.name == householdName }
        } else if (households.size == 1) {
            households.first()
        } else {
            null
        }

        if (household == null) {
            logger.error { "Could not find household \"$householdName\"" }
            return
        }

        val creativeTonies = tonieHandler.getCreativeTonies(household)
        val tonie = creativeTonies.find { it.name == tonieName }

        if (tonie == null) {
            logger.error { "Could not find tonie \"$tonieName\"" }
            return
        }

        val chapters = tonie.chapters
        logger.info("Chapters on tonie $tonieName: " + tonie.chaptersPresent)
        val audioFilesToSync = fileManager.getAudioFiles(date = LocalDate.now())
        val filesToUpload =
            audioFilesToSync.filterNot { chapters.map { it.title }.contains(it.nameWithoutExtension) }
        val chaptersToDelete =
            chapters.filterNot { audioFilesToSync.map { it.nameWithoutExtension }.contains(it.title) }
        logger.info("Chapters to upload (${filesToUpload.size}):")
        logger.info("$filesToUpload")
        logger.info("Chapters to delete ( ${chaptersToDelete.size}):")
        logger.info("$chaptersToDelete")
        if (!dryrun) {
            for (chapter in chaptersToDelete) {
                tonie.deleteChapter(chapter)
            }
            for (upload in filesToUpload) {
                tonie.uploadFile(upload.nameWithoutExtension, upload.absolutePath)
            }
            tonie.commit()
        }
        tonie.refresh()
        logger.info("Chapters on tonie $tonieName: " + tonie.chaptersPresent)
        logger.info("Remaining seconds on tonie ${tonie.secondsRemaining}")
        logger.info("Seconds on tonie ${tonie.secondsPresent}")

        tonieHandler.disconnect()
    }


}