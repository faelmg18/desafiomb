package br.com.auth.di

import br.com.auth.biometric.BiometricLoginManager
import br.com.auth.biometric.LoginManager
import org.koin.dsl.module

val biometricModule = module {
    single<LoginManager> { BiometricLoginManager(get(), get()) }
}

val authModules = listOf(biometricModule)

