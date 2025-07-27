package br.com.login.data.repository

import br.com.login.domain.repository.LoginRepository
import br.com.shared.data.datasource.local.UserLocalDataSource
import br.com.shared.domain.model.LoggedInUser

class LoginRepositoryImpl(
    val userLocalDataSource: UserLocalDataSource
) : LoginRepository {

    override fun isLoggedIn(): Boolean = userLocalDataSource.getUserUUID() != null

    override fun login(loggedInUser: LoggedInUser) {
        val userLogged = loggedInUser
        userLocalDataSource.save(userLogged)
    }
}