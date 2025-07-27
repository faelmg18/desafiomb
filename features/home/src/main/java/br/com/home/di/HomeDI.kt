package br.com.home.di

import br.com.home.data.datasource.local.MessageLocalDataSource
import br.com.home.data.datasource.local.MessageLocalDataSourceImpl
import br.com.home.data.datasource.remote.MessageRemoteDataSource
import br.com.home.data.datasource.remote.MessageRemoteDataSourceImpl
import br.com.home.data.datasource.remote.api.HomeApi
import br.com.home.data.repository.HomeRepositoryImpl
import br.com.home.data.repository.MessageRepositoryImpl
import br.com.home.data.repository.MockMessageRepositoryImpl
import br.com.home.domain.repository.HomeRepository
import br.com.home.domain.repository.MessageRepository
import br.com.home.ui.HomeViewModel
import br.com.home.ui.dvdview.DvdViewModel
import br.com.home.ui.user.UserViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val homeRepositoryModules = module {
    single<MessageRepository>(qualifier = named("real")) {
        MessageRepositoryImpl(get(), get())
    }

    single<MessageRepository>(qualifier = named("mock")) {
        MockMessageRepositoryImpl()
    }

    single<HomeRepository> { HomeRepositoryImpl(get()) }
}

private val homeDataSourceModules = module {
    single<MessageRemoteDataSource> { MessageRemoteDataSourceImpl(get()) }
    single<MessageLocalDataSource> { MessageLocalDataSourceImpl(get(), get()) }
}

private val networkModule = module {

    single<Gson> {
        GsonBuilder().create()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>()).build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<HomeApi> {
        get<Retrofit>().create(HomeApi::class.java)
    }
}

private val viewModelModule = module {
    viewModel {
        DvdViewModel(
            realMessageRepository = get(named("real")),
            mockMessageRepository = get(named("mock"))
        )
    }
    viewModel { UserViewModel(get()) }

    viewModel { HomeViewModel(get()) }
}

val homeModules =
    listOf(viewModelModule, networkModule, homeRepositoryModules, homeDataSourceModules)



