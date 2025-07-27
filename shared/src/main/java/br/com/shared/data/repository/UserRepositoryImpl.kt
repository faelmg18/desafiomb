package br.com.shared.data.repository

import br.com.shared.data.datasource.local.UserLocalDataSource
import br.com.shared.domain.model.LoggedInUser
import br.com.shared.domain.repository.UserRepository

class UserRepositoryImpl(private val dataSource: UserLocalDataSource) : UserRepository {
    override fun getUser(): LoggedInUser? {
        return dataSource.getUserUUID()
    }
}