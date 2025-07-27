package br.com.home.data.datasource.remote.api

import br.com.home.data.datasource.remote.model.MessageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {
    @GET("todos/{id}")
    suspend fun getMessage(@Path("id") lastId: Int): MessageResponse
}