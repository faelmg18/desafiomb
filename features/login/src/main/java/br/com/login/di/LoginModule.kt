package br.com.login.di

import br.com.login.data.repository.LoginRepositoryImpl
import br.com.login.domain.repository.LoginRepository
import br.com.login.ui.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}

private val loginViewModel = module {
    viewModel { LoginViewModel(get()) }
}

val loginModules = listOf(repositoryModule, loginViewModel)


