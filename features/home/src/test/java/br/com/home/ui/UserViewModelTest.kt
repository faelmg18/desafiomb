import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.shared.domain.model.LoggedInUser
import br.com.shared.domain.repository.UserRepository
import br.com.home.ui.user.UserViewModel
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository: UserRepository = mockk()
    private lateinit var userViewModel: UserViewModel
    private val observer: Observer<LoggedInUser> = mockk(relaxed = true)

    @Before
    fun setup() {
        userViewModel = UserViewModel(userRepository)
        userViewModel.userResult.observeForever(observer)
    }

    @Test
    fun `getUser should post user when user exists`() {
        val user = LoggedInUser(userUUID = "123",)
        every { userRepository.getUser() } returns user

        userViewModel.getUser()

        verify { observer.onChanged(user) }
    }

    @Test
    fun `getUser should not post when user is null`() {
        every { userRepository.getUser() } returns null

        userViewModel.getUser()

        verify(exactly = 0) { observer.onChanged(any()) }
    }
}