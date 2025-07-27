import br.com.shared.data.datasource.local.UserLocalDataSource
import br.com.home.data.repository.HomeRepositoryImpl
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class HomeRepositoryTest {

    private val userLocalDataSource: UserLocalDataSource = mockk()
    private lateinit var repository: HomeRepositoryImpl

    @Before
    fun setup() {
        repository = HomeRepositoryImpl(userLocalDataSource)
    }

    @Test
    fun `logout should call clear on userLocalDataSource`() {
        justRun { userLocalDataSource.clear() }

        repository.logout()

        verify(exactly = 1) { userLocalDataSource.clear() }
    }
}
