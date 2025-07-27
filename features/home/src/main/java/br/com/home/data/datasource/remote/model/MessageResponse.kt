package br.com.home.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("completed")
    val completed: Boolean
)
