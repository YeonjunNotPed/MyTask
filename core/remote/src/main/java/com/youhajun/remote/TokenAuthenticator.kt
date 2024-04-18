package com.youhajun.remote

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val remoteModuleBridge: RemoteModuleBridge
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

        val newToken = remoteModuleBridge.fetchAndSaveNewToken()
        return generateRequestWithNewToken(response, newToken)
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
        url.toString().contains(Endpoint.Auth.GET_REFRESHED_TOKEN)
}