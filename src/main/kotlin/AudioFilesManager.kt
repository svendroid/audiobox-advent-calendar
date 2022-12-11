import java.io.File
import java.time.LocalDate
import kotlin.math.min

class AudioFilesManager(
    private val folder: File,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val maxTotalDurationInSeconds: Long = 90 * 60
) {

    init {
        startDate
    }

    // get all files which should be synced for given date
    fun getAudioFiles(date: LocalDate = LocalDate.now()): List<File> {
        val range = startDate.rangeTo(endDate)
        if (!range.contains(date)) {
            throw IllegalStateException("$date is outside calendar range $range")
        }

        val days = startDate.datesUntil(date.plusDays(1)).count().toInt()

        val endIndex = min(days, (folder.listFiles()?.size) ?: 0)

        val allAudios = folder
            .listFiles()
            ?.toList()
            ?.sortedBy { it.nameWithoutExtension.filter { it.isDigit() }.toInt() }
            ?.subList(0, endIndex)
            ?: emptyList()

        var totalDuration = 0L
        val resultFiles = mutableListOf<File>()
        for (audio in allAudios.asReversed()) {
            val duration = AudioUtil.getAudioFileDurationInSeconds(audio.path)
            if ((duration + totalDuration) < maxTotalDurationInSeconds) {
                resultFiles.add(audio)
                totalDuration += duration
            } else {
                break
            }
        }
        return resultFiles.reversed()
    }

}