package br.com.desafiomercantil.di

import br.com.desafiomercantil.navigation.home.HomeNavigationImpl
import br.com.desafiomercantil.navigation.login.LoginNavigationImpl
import br.com.home.navigation.HomeNavigation
import br.com.login.navigation.LoginNavigation
import org.koin.dsl.module

private val navigationModule = module {
    single<LoginNavigation> { LoginNavigationImpl() }
    single<HomeNavigation> { HomeNavigationImpl() }
}

val appModules = listOf(navigationModule)