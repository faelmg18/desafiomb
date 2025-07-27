package br.com.home.domain.repository

import br.com.desafiomercantil.core.remote.Result
import br.com.home.domain.model.Message

interface MessageRepository {
    suspend fun getMessage(lastId: Int = 1): Result<Message>
    fun readAllMessages(): List<Message>
    fun saveLocalMessage(message: Message)
}