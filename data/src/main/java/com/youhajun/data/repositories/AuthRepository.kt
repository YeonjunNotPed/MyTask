package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.encrypt.Crypto
import com.youhajun.data.models.dto.auth.MyTaskToken
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.localDataSource.PrefDataStoreDataSource
import com.youhajun.data.repositories.remoteDataSource.AuthRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val crypto: Crypto,
    private val prefDataStoreDataSource: PrefDataStoreDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : BaseRepository() {

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
    }

    fun getRefreshedMyTaskToken(refreshToken: String): Flow<Resource<MyTaskToken>> =
        authRemoteDataSource.getRefreshedToken(refreshToken).map { apiConverter(it) }

    fun getAccessTokenSync(): Resource<String> {
        return prefDataStoreDataSource.getStringSync(ACCESS_TOKEN_KEY)
    }

    fun getRefreshTokenSync(): Flow<Resource<String>> = flow {
        val token = prefDataStoreDataSource.getStringSync(REFRESH_TOKEN_KEY)
        val result = if (token is Resource.Success) Resource.Success(crypto.decrypt(token.data))
        else token
        emit(result)
    }

    fun getRefreshToken(): Flow<Resource<String>> {
        return prefDataStoreDataSource.getString(REFRESH_TOKEN_KEY).map {
            when(it) {
                is Resource.Success -> Resource.Success(crypto.decrypt(it.data))
                is Resource.Error -> Resource.Error(it.errorVo.convert())
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    fun getAccessToken(): Flow<Resource<String>> {
        return prefDataStoreDataSource.getString(ACCESS_TOKEN_KEY).map {
            when (it) {
                is Resource.Success -> Resource.Success(crypto.decrypt(it.data))
                is Resource.Error -> Resource.Error(it.errorVo.convert())
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun saveMyTaskToken(myTaskToken: MyTaskToken): Flow<Resource<Unit>> {
        val accessTokenArray = crypto.encrypt(myTaskToken.accessToken)
        val refreshTokenArray = crypto.encrypt(myTaskToken.refreshToken)
        return prefDataStoreDataSource.setString(ACCESS_TOKEN_KEY, accessTokenArray).flatMapConcat {
            when (it) {
                is Resource.Success -> prefDataStoreDataSource.setString(REFRESH_TOKEN_KEY, refreshTokenArray)
                is Resource.Error -> flow { emit(Resource.Error(it.errorVo)) }
                is Resource.Loading -> emptyFlow()
            }
        }
    }
}

