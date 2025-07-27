import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.home.domain.repository.HomeRepository
import br.com.home.ui.HomeViewModel
import br.com.home.ui.state.HomeActions
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val homeRepository: HomeRepository = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun `logout should call repository logout and send NavigateToLogin action`() = runTest {
        viewModel.logout()

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { homeRepository.logout() }

        val action = viewModel.actions.first()
        assertEquals(HomeActions.NavigateToLogin, action)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
