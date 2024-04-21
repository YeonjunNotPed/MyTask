package com.youhajun.data.repositories

import com.youhajun.data.encrypt.Crypto
import com.youhajun.data.repositories.localDataSource.PrefDataStoreDataSource
import com.youhajun.data.repositories.remoteDataSource.AuthRemoteDataSource
import com.youhajun.model_data.login.MyTaskToken
import com.youhajun.model_data.ApiResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val crypto: Crypto,
    private val prefDataStoreDataSource: PrefDataStoreDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
    }

    suspend fun getRefreshedMyTaskToken(refreshToken: String): ApiResult<MyTaskToken> = authRemoteDataSource.getRefreshedToken(refreshToken)

    fun getAccessTokenSync(): String = prefDataStoreDataSource.getStringSync(ACCESS_TOKEN_KEY)

    fun getRefreshTokenSync(): String {
        val token = prefDataStoreDataSource.getStringSync(REFRESH_TOKEN_KEY)
        return crypto.decrypt(token)
    }

    suspend fun saveMyTaskToken(myTaskToken: MyTaskToken) {
        val accessTokenArray = crypto.encrypt(myTaskToken.accessToken)
        val refreshTokenArray = crypto.encrypt(myTaskToken.refreshToken)
        prefDataStoreDataSource.setString(ACCESS_TOKEN_KEY, accessTokenArray)
        prefDataStoreDataSource.setString(REFRESH_TOKEN_KEY, refreshTokenArray)
    }
}

