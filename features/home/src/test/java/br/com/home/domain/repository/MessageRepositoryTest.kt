import br.com.desafiomercantil.core.remote.Result
import br.com.home.data.datasource.local.MessageLocalDataSource
import br.com.home.data.datasource.remote.MessageRemoteDataSource
import br.com.home.domain.model.Message
import br.com.home.data.repository.MessageRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MessageRepositoryTest {

    private val remoteDataSource: MessageRemoteDataSource = mockk()
    private val localDataSource: MessageLocalDataSource = mockk()
    private lateinit var repository: MessageRepositoryImpl

    @Before
    fun setup() {
        repository = MessageRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getMessage should return result from remoteDataSource`() = runTest {
        val lastId = 10
        val message = Message(userId = 1, id = 11, title = "Title", completed = false)
        val expectedResult: Result<Message> = Result.Success(message)

        coEvery { remoteDataSource.getMessage(lastId) } returns expectedResult

        val result = repository.getMessage(lastId)

        assertTrue(result is Result.Success)
        assertEquals(message, (result as Result.Success).data)
        coEvery { remoteDataSource.getMessage(lastId) }
    }

    @Test
    fun `readAllMessages should return messages from localDataSource`() {
        val messages = listOf(
            Message(1, 1, "Title 1", false),
            Message(2, 2, "Title 2", true)
        )

        io.mockk.every { localDataSource.loadAllMessageLocal() } returns messages

        val result = repository.readAllMessages()

        assertEquals(messages, result)
        verify(exactly = 1) { localDataSource.loadAllMessageLocal() }
    }

    @Test
    fun `saveLocalMessage should call localDataSource saveLocalMessage`() {
        val message = Message(1, 1, "Title", false)

        io.mockk.justRun { localDataSource.saveLocalMessage(message) }

        repository.saveLocalMessage(message)

        verify(exactly = 1) { localDataSource.saveLocalMessage(message) }
    }
}