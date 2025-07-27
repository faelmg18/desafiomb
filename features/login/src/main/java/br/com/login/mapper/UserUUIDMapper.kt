package br.com.login.mapper

import br.com.shared.domain.model.LoggedInUser

fun toLoggedInUser(userUUID: String) = LoggedInUser(userUUID)