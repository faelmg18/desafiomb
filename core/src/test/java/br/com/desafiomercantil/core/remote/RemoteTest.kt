package br.com.desafiomercantil.core.remote

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DoRequestTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun `test doRequest returns Success when request succeeds`() = runTest {
        val expected = "Hello"
        val result = doRequest(dispatcher) {
            expected
        }

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
    }

    @Test
    fun `test doRequest returns Error when IOException is thrown`() = runTest {
        val exception = IOException("Network error")

        val result = doRequest(dispatcher) {
            throw exception
        }

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `test doRequest returns Error when generic Exception is thrown`() = runTest {
        val exception = IllegalStateException("error")

        val result = doRequest(dispatcher) {
            throw exception
        }

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}
