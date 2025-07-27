package br.com.home.domain.mapper

import br.com.home.domain.model.Message
import br.com.home.ui.components.DvdMessage

fun List<Message>.toDvdMessages() = map {
    DvdMessage(
        id = it.id,
        message = it.title
    )
}