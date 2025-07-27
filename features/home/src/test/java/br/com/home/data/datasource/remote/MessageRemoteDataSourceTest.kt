package br.com.home.data.datasource.remote

import br.com.desafiomercantil.core.remote.Result
import br.com.home.data.datasource.remote.api.HomeApi
import br.com.home.data.datasource.remote.model.MessageResponse
import br.com.home.data.mapper.toDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MessageRemoteDataSourceTest {

    private val api: HomeApi = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var remoteDataSource: MessageRemoteDataSourceImpl

    @Before
    fun setUp() {
        remoteDataSource = MessageRemoteDataSourceImpl(api, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMessage returns success result when api call succeeds`() = runTest {
        val lastId = 5
        val apiResponse: MessageResponse = mockk(relaxed = true)

        coEvery { api.getMessage(lastId + 1) } returns apiResponse

        val result = remoteDataSource.getMessage(lastId)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(result is Result.Success)
        assertEquals(apiResponse.toDomain(), result.data)
        coVerify(exactly = 1) { api.getMessage(lastId + 1) }
    }

    @Test
    fun `getMessage returns failure result when api call throws exception`() = runTest {
        val lastId = 10
        val exception = RuntimeException("API failure")

        coEvery { api.getMessage(lastId + 1) } throws exception

        val result = remoteDataSource.getMessage(lastId)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(result is Result.Error)
        coVerify(exactly = 1) { api.getMessage(lastId + 1) }
    }
}
