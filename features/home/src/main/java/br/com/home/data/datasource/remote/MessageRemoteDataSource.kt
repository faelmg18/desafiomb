package br.com.home.data.datasource.remote

import br.com.desafiomercantil.core.remote.Result
import br.com.home.domain.model.Message

interface MessageRemoteDataSource {
    suspend fun getMessage(lastId: Int = 1): Result<Message>
}