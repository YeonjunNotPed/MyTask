package com.youhajun.data.network

import android.util.Log
import com.youhajun.data.Endpoint
import com.youhajun.data.Resource
import com.youhajun.data.repositories.AuthRepository
import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationInterceptor @Inject constructor(
    private val authRepository: Lazy<AuthRepository>
) : Interceptor {
    companion object {
        private const val TAG: String = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if(chain.request().isWithoutAuthTokenRequest()) {
            return chain.proceed(chain.request())
        }

        val authRepo = authRepository.get()
        val token = fetchAccessToken(authRepo)
        val request = if(token.isNullOrEmpty()) chain.request() else requestWithToken(chain, token)

        Log.d(TAG, "AuthenticationInterceptor - intercept() called / request header: ${request.headers}")
        return chain.proceed(request)
    }

    private val withoutAuthTokenEndpointList = listOf(
        Endpoint.Auth.GET_REFRESHED_TOKEN,
        Endpoint.Sign.POST_LOGIN
    )

    private fun Request.isWithoutAuthTokenRequest() = withoutAuthTokenEndpointList.any {
        url.toString().contains(it)
    }

    private fun fetchAccessToken(authRepo: AuthRepository): String? {
        return authRepo.getAccessTokenSync().run {
            if(this is Resource.Success) data else null
        }
    }

    private fun requestWithToken(chain: Interceptor.Chain, token: String): Request {
        return chain
            .request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
    }
}