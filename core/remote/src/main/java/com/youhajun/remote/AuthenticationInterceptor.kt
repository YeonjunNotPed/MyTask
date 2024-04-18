package com.youhajun.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val remoteModuleBridge: RemoteModuleBridge
) : Interceptor {
    companion object {
        private const val TAG: String = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if(chain.request().isWithoutAuthTokenRequest()) {
            return chain.proceed(chain.request())
        }

        val token = remoteModuleBridge.fetchAccessToken()
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

    private fun requestWithToken(chain: Interceptor.Chain, token: String): Request {
        return chain
            .request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
    }
}