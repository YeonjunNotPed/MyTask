package com.youhajun.ui.utils

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class KakaoLoginUtil(
    @ActivityContext private var _context: Context?
) {

    private val context get() = _context!!

    fun kakaoLogin(onSuccess:(OAuthToken) -> Unit) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginKakaoApp(onSuccess)
        } else {
            loginKakaoEmail(onSuccess)
        }
    }

    private fun loginKakaoApp(onSuccess:(OAuthToken) -> Unit) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            token?.let {
                onSuccess(it)
            } ?: run {
                val userCancel = error is ClientError? && error?.reason == ClientErrorCause.Cancelled
                if (!userCancel) loginKakaoEmail(onSuccess)
            }
        }
    }

    private fun loginKakaoEmail(onSuccess:(OAuthToken) -> Unit) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            token?.let {
                onSuccess(it)
            }
        }
    }

    fun onDispose() {
        _context = null
    }
}