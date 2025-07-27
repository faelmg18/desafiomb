package br.com.home.data.datasource.remote

import br.com.desafiomercantil.core.remote.Result
import br.com.desafiomercantil.core.remote.doRequest
import br.com.home.data.datasource.remote.api.HomeApi
import br.com.home.data.mapper.toDomain
import br.com.home.domain.model.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MessageRemoteDataSourceImpl(
    private val api: HomeApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessageRemoteDataSource {
    override suspend fun getMessage(lastId: Int): Result<Message> =
        doRequest(dispatcher = dispatcher) {
            api.getMessage(lastId + 1).toDomain()
        }
}