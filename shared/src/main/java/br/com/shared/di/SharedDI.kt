package br.com.shared.di

import br.com.shared.data.datasource.local.UserLocalDataSource
import br.com.shared.data.datasource.local.UserLocalDataSourceImpl
import br.com.shared.data.repository.UserRepositoryImpl
import br.com.shared.domain.repository.UserRepository
import org.koin.dsl.module

private val dataSourceModule = module {
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get()) }
}

private val repositoryModules = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val sharedModules = listOf(dataSourceModule, repositoryModules)