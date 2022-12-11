import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

internal class AventcalendarTest {

    lateinit var underTest: AudioFilesManager

    @BeforeEach
    internal fun setUp() {
        mockkStatic(LocalDate::class)

        val folder = mockk<File>()
        val fileMocks: Array<File> = listOf(
            "title-chapter-1-episode",
            "title-chapter-2-episode",
            "title-chapter-3-episode",
            "title-chapter-4-episode",
            "title-chapter-5-episode",
            "title-chapter-6-episode",
            "title-chapter-7-episode",
            "title-chapter-8-episode",
            "title-chapter-9-episode",
            "title-chapter-10-episode",
            "title-chapter-11-episode",
            "title-chapter-12-episode",
            "title-chapter-13-episode",
            "title-chapter-14-episode",
            "title-chapter-15-episode",
            "title-chapter-16-episode",
            "title-chapter-17-episode",
            "title-chapter-18-episode",
            "title-chapter-19-episode",
            "title-chapter-20-episode",
            "title-chapter-21-episode",
            "title-chapter-22-episode",
            "title-chapter-23-episode",
            "title-chapter-24-episode",
        ).map {
            val mock: File = mockk<File>()
            every { mock.nameWithoutExtension }.returns(it)
            every { mock.path }.returns(it)
            mock
        }.toTypedArray()
        every { folder.listFiles() }.returns(fileMocks)

        mockkObject(AudioUtil.Companion)
        every { AudioUtil.getAudioFileDurationInSeconds(any()) }.returns(1)

        underTest = AudioFilesManager(
            folder = folder,
            startDate = LocalDate.of(2022, 12, 1),
            endDate = LocalDate.of(2022, 12, 24)
        )
    }

    @AfterEach
    internal fun tearDown() {
        unmockkStatic(LocalDate::class)
    }

    @Test
    fun `getAudioFiles on 4-12 returns 4 files`() {
        //Given
        every { LocalDate.now() }.returns(LocalDate.of(2022, 12, 4))

        //When
        val audioFiles = underTest.getAudioFiles()

        //Then
        assertThat(audioFiles.size).isEqualTo(4)
    }

    @Test
    fun `getAudioFiles on 2-12 returns 2 files`() {
        //Given
        every { LocalDate.now() }.returns(LocalDate.of(2022, 12, 2))

        //When
        val audioFiles = underTest.getAudioFiles()

        //Then
        assertThat(audioFiles.size).isEqualTo(2)
    }

    @Test
    fun `getAudioFiles is sorted by number in filename eg 10 is after 9`() {
        //Given
        every { LocalDate.now() }.returns(LocalDate.of(2022, 12, 12))

        //When
        val audioFiles = underTest.getAudioFiles()

        //Then
        for (audioFile in audioFiles) {
            println(audioFile.nameWithoutExtension)
        }
        assertThat(audioFiles.map { it.nameWithoutExtension }).isEqualTo(
            listOf(
                "title-chapter-1-episode",
                "title-chapter-2-episode",
                "title-chapter-3-episode",
                "title-chapter-4-episode",
                "title-chapter-5-episode",
                "title-chapter-6-episode",
                "title-chapter-7-episode",
                "title-chapter-8-episode",
                "title-chapter-9-episode",
                "title-chapter-10-episode",
                "title-chapter-11-episode",
                "title-chapter-12-episode",
            )
        )
    }
}