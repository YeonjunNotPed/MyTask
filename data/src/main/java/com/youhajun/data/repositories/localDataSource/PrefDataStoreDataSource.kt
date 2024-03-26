package com.youhajun.data.repositories.localDataSource

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.youhajun.data.Resource
import com.youhajun.data.ResourceErrorVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

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

    fun getStringSync(key: String): Resource<String> =
        getSyncPreferencesData(stringPreferencesKey(key))

    fun getBoolean(key: String): Flow<Resource<Boolean>> =
        getPreferencesData(booleanPreferencesKey(key))

    fun setBoolean(key: String, value: Boolean): Flow<Resource<Unit>> =
        savePreferencesData(booleanPreferencesKey(key), value)

    fun getString(key: String): Flow<Resource<String>> =
        getPreferencesData(stringPreferencesKey(key))

    fun setString(key: String, value: String): Flow<Resource<Unit>> =
        savePreferencesData(stringPreferencesKey(key), value)

    fun getInt(key: String): Flow<Resource<Int>> =
        getPreferencesData(intPreferencesKey(key))

    fun setInt(key: String, value: Int): Flow<Resource<Unit>> =
        savePreferencesData(intPreferencesKey(key), value)

    fun getByteArray(key: String): Flow<Resource<ByteArray>> {
        return getString(key).map {
            when (it) {
                is Resource.Success -> Resource.Success(base64ToByteArray(it.data))
                is Resource.Error -> Resource.Error(it.errorVo.convert())
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    fun setByteArray(key: String, value: ByteArray): Flow<Resource<Unit>> =
        setString(key, byteArrayToBase64(value))

    private fun <T> getPreferencesData(key: Preferences.Key<T>): Flow<Resource<T>> {
        return dataStorePref.data.map { pref ->
            pref[key]?.let { data ->
                Resource.Success(data)
            } ?: Resource.Error(ResourceErrorVo())
        }
    }

    private fun <T> savePreferencesData(
        key: Preferences.Key<T>,
        value: T
    ): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        dataStorePref.edit { pref -> pref[key] = value }
        emit(Resource.Success(Unit))
    }.catch { emit(Resource.Error(ResourceErrorVo())) }

    private fun <T> getSyncPreferencesData(key: Preferences.Key<T>): Resource<T> = runBlocking {
        dataStorePref.data.first()[key]?.let {
            Resource.Success(it)
        } ?: Resource.Error(ResourceErrorVo())
    }
}