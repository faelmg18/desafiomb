package br.com.desafiomercantil.core.di

import android.content.SharedPreferences
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.desafiomercantil.core.cacheprovider.CacheProviderImpl
import br.com.desafiomercantil.core.di.providers.provideEncryptedSharedPreferences
import br.com.desafiomercantil.core.uuid.MercantilUUID
import br.com.desafiomercantil.core.uuid.MercantilUUIDImpl
import org.koin.dsl.module

val cacheModule = module {
    single<SharedPreferences> {
        provideEncryptedSharedPreferences(get())
    }

    single<CacheProvider> {
        CacheProviderImpl(get())
    }
}

val uuid = module {
    single<MercantilUUID> { MercantilUUIDImpl(get(), get()) }
}

val coreModules = listOf(cacheModule, uuid)
