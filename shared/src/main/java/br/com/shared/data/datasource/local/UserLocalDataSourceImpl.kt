package br.com.shared.data.datasource.local

import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.shared.domain.model.LoggedInUser
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class UserLocalDataSourceImpl(
    private val cacheProvider: CacheProvider,
    private val gson: Gson = GsonBuilder().create()
) : UserLocalDataSource {

    companion object {
        private const val USER_KEY = "USER_PREFERENCE_KEY"
    }

    override fun getUserUUID(): LoggedInUser? {
        val json = cacheProvider.getString(USER_KEY)
        return json?.let {
            gson.fromJson(json, LoggedInUser::class.java)
        }
    }

    override fun save(loggedInUser: LoggedInUser) {
        val json = gson.toJson(loggedInUser)
        cacheProvider.saveString(json, USER_KEY)
    }

    override fun clear() {
        cacheProvider.clearByKey(USER_KEY)
    }
}