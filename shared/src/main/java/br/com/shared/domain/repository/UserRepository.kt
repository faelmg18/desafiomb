package br.com.shared.domain.repository

import br.com.shared.domain.model.LoggedInUser

interface UserRepository {
    fun getUser(): LoggedInUser?
}