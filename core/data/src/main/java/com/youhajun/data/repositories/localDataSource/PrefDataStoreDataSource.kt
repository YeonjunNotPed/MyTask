package com.youhajun.data.repositories.localDataSource

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PrefDataStoreDataSource @Inject constructor(
    private val dataStorePref: DataStore<Preferences>
) {
    companion object {
        fun byteArrayToBase64(byteArray: ByteArray): String {
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun base64ToByteArray(base64: String): ByteArray {
            return Base64.decode(base64, Base64.DEFAULT)
        }
    }

    fun getStringSync(key: String): String =
        getSyncPreferencesData(stringPreferencesKey(key), "")

    fun getBoolean(key: String): Flow<Boolean> =
        getPreferencesData(booleanPreferencesKey(key), false)

    suspend fun setBoolean(key: String, value: Boolean): Preferences =
        savePreferencesData(booleanPreferencesKey(key), value)

    fun getString(key: String): Flow<String> =
        getPreferencesData(stringPreferencesKey(key), "")

    suspend fun setString(key: String, value: String): Preferences =
        savePreferencesData(stringPreferencesKey(key), value)

    fun getInt(key: String): Flow<Int> =
        getPreferencesData(intPreferencesKey(key), -1)

    suspend fun setInt(key: String, value: Int): Preferences =
        savePreferencesData(intPreferencesKey(key), value)

    fun getByteArray(key: String): Flow<ByteArray> =
        getString(key).map { base64ToByteArray(it) }

    suspend fun setByteArray(key: String, value: ByteArray): Preferences =
        setString(key, byteArrayToBase64(value))

    private fun <T> getPreferencesData(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStorePref.data.map { pref -> pref[key] ?: default }

    private suspend fun <T> savePreferencesData(
        key: Preferences.Key<T>,
        value: T
    ) = dataStorePref.edit { pref -> pref[key] = value }

    private fun <T> getSyncPreferencesData(key: Preferences.Key<T>, default: T): T = runBlocking {
        return@runBlocking dataStorePref.data.first()[key] ?: default
    }
}