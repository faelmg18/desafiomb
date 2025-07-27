package br.com.home.domain.model

data class Message(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
)