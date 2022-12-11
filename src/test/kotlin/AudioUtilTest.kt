import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class AudioUtilTest{

    @Test
    fun `getAudioFileDurationInSeconds should retrun correct duration for sample`() {
        //Given
        val testFilePath = "testData/mp3_sample-15s.mp3"

        //When
        val audioFiles = AudioUtil.getAudioFileDurationInSeconds(testFilePath)

        //Then
        assertThat(audioFiles).isEqualTo(19)
    }
}