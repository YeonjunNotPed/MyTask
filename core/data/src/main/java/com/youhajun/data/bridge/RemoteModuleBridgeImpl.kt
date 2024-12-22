package com.youhajun.data.bridge

import com.youhajun.data.repositories.AuthRepository
import com.youhajun.model_data.ApiSuccess
import com.youhajun.remote.RemoteModuleBridge
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RemoteModuleBridgeImpl @Inject constructor(
    private val authRepository: Lazy<AuthRepository>
): RemoteModuleBridge {

    override fun fetchAccessToken(): String? {
        val token = authRepository.get().getAccessTokenSync()
        return token.ifEmpty { null }
    }

    override fun fetchAndSaveNewToken(): String? = runBlocking {
        val repo = authRepository.get()
        val refreshToken = repo.getRefreshTokenSync()
        val newToken = repo.getRefreshedMyTaskToken(refreshToken)
        return@runBlocking if(newToken is ApiSuccess) {
            repo.saveMyTaskToken(newToken.data)
            newToken.data.accessToken
        } else null
    }
}