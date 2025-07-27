package br.com.home.data.repository

import br.com.desafiomercantil.core.remote.Result
import br.com.home.data.datasource.local.MessageLocalDataSource
import br.com.home.data.datasource.remote.MessageRemoteDataSource
import br.com.home.domain.model.Message
import br.com.home.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val messageLocalDataSource: MessageLocalDataSource
) : MessageRepository {
    override suspend fun getMessage(lastId: Int): Result<Message> {
        return messageRemoteDataSource.getMessage(lastId)
    }

    override fun readAllMessages(): List<Message> {
        return messageLocalDataSource.loadAllMessageLocal()
    }

    override fun saveLocalMessage(message: Message) {
        messageLocalDataSource.saveLocalMessage(message)
    }
}