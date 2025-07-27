package br.com.home.domain.model

data class MessageResult(
    val success: Message? = null,
    val error: Int? = null
)