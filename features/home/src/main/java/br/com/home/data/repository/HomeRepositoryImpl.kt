package br.com.home.data.repository

import br.com.home.domain.repository.HomeRepository
import br.com.shared.data.datasource.local.UserLocalDataSource

class HomeRepositoryImpl(private val userLocalDataSource: UserLocalDataSource) : HomeRepository {
    override fun logout() {
        userLocalDataSource.clear()
    }
}