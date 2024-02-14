package com.youhajun.data.network

import android.util.Log
import com.youhajun.data.Endpoint
import com.youhajun.data.Resource
import com.youhajun.data.repositories.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authRepository: Lazy<AuthRepository>
) : Authenticator {

    companion object {
        private const val TAG = "TokenAuth"
    }

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        val request = response.request

        if (isGiveUpAuthenticating(request, response)) {
            return null
        }

        val authRepo = authRepository.get()
        val newToken = fetchAndSaveNewToken(authRepo)
        return generateRequestWithNewToken(response, newToken)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Synchronized
    private fun fetchAndSaveNewToken(authRepo: AuthRepository): String? = runBlocking(Dispatchers.IO) {
        var newAccessToken:String? = null
        val result = authRepo.getRefreshTokenSync().flatMapConcat {
            when(it) {
                is Resource.Success -> authRepo.getRefreshedMyTaskToken(it.data)
                is Resource.Error -> flow { emit(Resource.Error(it.errorVo.convert())) }
                is Resource.Loading -> emptyFlow()
            }
        }.flatMapConcat {
            when(it) {
                is Resource.Success -> {
                    newAccessToken = it.data.accessToken
                    authRepo.saveMyTaskToken(it.data)
                }
                is Resource.Error -> flow { emit(Resource.Error(it.errorVo.convert())) }
                is Resource.Loading -> emptyFlow()
            }
        }.firstOrNull()

        if(result is Resource.Success) newAccessToken else null
    }

    private fun generateRequestWithNewToken(response: Response, newToken: String?): Request? {
        return newToken?.let {
            Log.d(TAG, "TokenAuthenticator - authenticate() called / 중단된 API 재요청")
            response.request
                .newBuilder()
                .removeHeader("Authorization")
                .header("Authorization", "Bearer $it")
                .build()
        }
    }

    // 토큰 리프레시 요청에서 401이 뜨거나 요청이 2번이 넘어가면 포기
    private fun isGiveUpAuthenticating(request: Request, response: Response): Boolean {
        return request.isGetRefreshedTokenRequest() || response.responseCount > 3
    }

    private fun Request.isGetRefreshedTokenRequest() =
        url.toString().contains(Endpoint.AUTH.GET_REFRESHED_TOKEN)
}