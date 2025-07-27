package br.com.desafiomercantil.core.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun <T> doRequest(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    request: suspend () -> T
): Result<T> {
    return try {
        val result = withContext(dispatcher) { request() }
        Result.Success(result)
    } catch (e: IOException) {
        Result.Error(e)
    } catch (e: Exception) {
        Result.Error(e)
    }
}