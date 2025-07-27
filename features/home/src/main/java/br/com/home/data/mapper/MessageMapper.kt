package br.com.home.data.mapper

import br.com.home.data.datasource.remote.model.MessageResponse
import br.com.home.domain.model.Message

fun MessageResponse.toDomain() = Message(
    userId = userId,
    id = id,
    title = title,
    completed = completed
)