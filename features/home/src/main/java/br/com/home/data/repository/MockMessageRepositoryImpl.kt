package br.com.home.data.repository

import br.com.desafiomercantil.core.remote.Result
import br.com.home.domain.model.Message
import br.com.home.domain.repository.MessageRepository
import kotlin.random.Random

class MockMessageRepositoryImpl : MessageRepository {
    override suspend fun getMessage(lastId: Int): Result<Message> {
        return Result.Success(
            Message(
                userId = Random.nextInt(),
                id = lastId,
                title = "Mensagem mock $lastId",
                completed = true
            )
        )
    }

    override fun readAllMessages(): List<Message> {
        // Pode retornar vazio ou outra lógica
        return emptyList()
    }

    override fun saveLocalMessage(message: Message) {
        // Pode ser ignorado nessa implementação
    }
}