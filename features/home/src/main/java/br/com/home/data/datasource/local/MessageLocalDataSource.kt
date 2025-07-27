package br.com.home.data.datasource.local

import br.com.home.domain.model.Message

interface MessageLocalDataSource {
    fun loadAllMessageLocal(): List<Message>
    fun saveLocalMessage(message: Message)
}