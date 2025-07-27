import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.desafiomercantil.core.remote.Result
import br.com.home.R
import br.com.home.domain.model.Message
import br.com.home.domain.model.MessageResult
import br.com.home.domain.repository.MessageRepository
import br.com.home.domain.mapper.toDvdMessages
import br.com.home.ui.components.DvdMessage
import br.com.home.ui.dvdview.DvdViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DvdViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val realRepository: MessageRepository = mockk()
    private val mockRepository: MessageRepository = mockk()
    private lateinit var viewModel: DvdViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DvdViewModel(realRepository, mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMessage with realRequest success updates messageResult with success`() = runTest {
        val message = Message(1, 1, "title", true)
        coEvery { realRepository.getMessage(any()) } returns Result.Success(message)

        val observer = mockk<Observer<MessageResult>>(relaxed = true)
        viewModel.messageResult.observeForever(observer)

        viewModel.getMessage(0)
        testScheduler.advanceUntilIdle()

        verify { observer.onChanged(MessageResult(success = message)) }
        coVerify { realRepository.getMessage(0) }

        viewModel.messageResult.removeObserver(observer)
    }

    @Test
    fun `getMessage with realRequest failure updates messageResult with error`() = runTest {
        coEvery { realRepository.getMessage(any()) } returns Result.Error(Exception())

        val observer = mockk<Observer<MessageResult>>(relaxed = true)
        viewModel.messageResult.observeForever(observer)

        viewModel.getMessage(0)
        testScheduler.advanceUntilIdle()

        verify { observer.onChanged(MessageResult(error = R.string.message_error)) }
        coVerify { realRepository.getMessage(0) }

        viewModel.messageResult.removeObserver(observer)
    }

    @Test
    fun `getMessage with mockRequest success updates messageResult with success`() = runTest {
        val message = Message(1, 1, "title", true)
        viewModel.toggleRealRequest()
        coEvery { mockRepository.getMessage() } returns Result.Success(message)

        val observer = mockk<Observer<MessageResult>>(relaxed = true)
        viewModel.messageResult.observeForever(observer)

        viewModel.getMessage(0)
        testScheduler.advanceUntilIdle()

        verify { observer.onChanged(MessageResult(success = message)) }
        coVerify { mockRepository.getMessage() }

        viewModel.messageResult.removeObserver(observer)
    }

    @Test
    fun `saveMessage calls saveLocalMessage on realRepository`() {
        val message = Message(1, 1, "title", true)
        every { realRepository.saveLocalMessage(message) } just Runs

        viewModel.saveMessage(message)

        verify { realRepository.saveLocalMessage(message) }
    }

    @Test
    fun `readAllMessagesLocal updates dvdMessageList with mapped messages`() {
        val messages = listOf(
            Message(1, 1, "title1", true),
            Message(2, 2, "title2", false)
        )
        every { realRepository.readAllMessages() } returns messages

        val observer = mockk<Observer<List<DvdMessage>>>(relaxed = true)
        viewModel.dvdMessageList.observeForever(observer)

        viewModel.readAllMessagesLocal()

        verify { observer.onChanged(messages.toDvdMessages()) }
        verify { realRepository.readAllMessages() }

        viewModel.dvdMessageList.removeObserver(observer)
    }

    @Test
    fun `toggleRealRequest toggles isRealRequest flag`() {
        coEvery { realRepository.getMessage(any()) } returns Result.Error(Exception())
        coEvery { mockRepository.getMessage() } returns Result.Error(Exception())

        runTest {
            viewModel.getMessage(0)
            testScheduler.advanceUntilIdle()
            coVerify { realRepository.getMessage(0) }

            viewModel.toggleRealRequest()

            viewModel.getMessage(0)
            testScheduler.advanceUntilIdle()
            coVerify { mockRepository.getMessage() }
        }
    }
}
