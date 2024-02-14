package com.youhajun.ui.utils

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import javax.inject.Singleton

object KakaoLoginUtil {

    fun kakaoLogin(context: Context, onSuccess:(OAuthToken) -> Unit) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginKakaoApp(context, onSuccess)
        } else {
            loginKakaoEmail(context, onSuccess)
        }
    }

    private fun loginKakaoApp(context: Context, onSuccess:(OAuthToken) -> Unit) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            token?.let {
                onSuccess(it)
            } ?: run {
                val userCancel = error is ClientError? && error?.reason == ClientErrorCause.Cancelled
                if (!userCancel) loginKakaoEmail(context, onSuccess)
            }
        }
    }

    private fun loginKakaoEmail(context: Context, onSuccess:(OAuthToken) -> Unit) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            token?.let {
                onSuccess(it)
            }
        }
    }
}