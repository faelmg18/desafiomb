package br.com.desafiomercantil.core.cacheprovider

import android.content.SharedPreferences

internal class CacheProviderImpl(
    private val sharedPreferences: SharedPreferences
) : CacheProvider {

    override fun saveString(value: String, key: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    override fun clearByKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}