package br.com.desafiomercantil.core.cacheprovider

interface CacheProvider {
    fun saveString(value: String, key: String)
    fun getString(key: String): String?
    fun clear()
    fun clearByKey(key: String)
}