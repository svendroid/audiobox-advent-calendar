import com.mpatric.mp3agic.Mp3File


class AudioUtil {

    companion object {

        fun getAudioFileDurationInSeconds(absolutePath: String): Long {
            return Mp3File(absolutePath).lengthInSeconds
        }

    }

}