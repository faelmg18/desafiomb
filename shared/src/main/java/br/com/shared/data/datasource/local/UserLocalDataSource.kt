package br.com.shared.data.datasource.local

import br.com.shared.domain.model.LoggedInUser

interface UserLocalDataSource {
    fun getUserUUID(): LoggedInUser?
    fun save(loggedInUser: LoggedInUser)
    fun clear()
}