package br.com.home.data.datasource.local

import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.home.domain.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageLocalDataSourceImpl(private val gson: Gson, private val cacheProvider: CacheProvider) :
    MessageLocalDataSource {

    companion object {
        const val MESSAGE_KEY = "MESSAGE_KEY"
    }

    override fun loadAllMessageLocal(): List<Message> {
        val messageJson = cacheProvider.getString(MESSAGE_KEY)
        val messages = messageJson?.fromJsonToList<Message>() ?: emptyList()
        return messages
    }

    override fun saveLocalMessage(message: Message) {
        val messages = loadAllMessageLocal().toMutableList()
        if (messages.size > 256) {
            return
        }

        messages.add(message)
        val json = gson.toJson(messages)
        cacheProvider.saveString(json, MESSAGE_KEY)
    }
}

private inline fun <reified T> String.fromJsonToList(): List<T> {
    return try {
        val type = object : TypeToken<List<T>>() {}.type
        Gson().fromJson(this, type)
    } catch (e: Exception) {
        emptyList()
    }
}