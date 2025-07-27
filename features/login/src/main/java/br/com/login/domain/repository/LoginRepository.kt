package br.com.login.domain.repository

import br.com.shared.domain.model.LoggedInUser

interface LoginRepository {
    fun login(loggedInUser: LoggedInUser)
    fun isLoggedIn(): Boolean
}