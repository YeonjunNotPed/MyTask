package com.youhajun.ui.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.youhajun.ui.BuildConfig
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class GoogleLoginUtil(
    @ActivityContext private var _context: Context?
) {
    private val context get() = _context!!

    fun checkAlreadyGoogleLogin(alreadyGoogleLogin: (GoogleSignInAccount) -> Unit) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?.let {
            alreadyGoogleLogin(it)
        }
    }

    fun getGoogleLoginIntent(): Intent {
        val apiServerId = BuildConfig.GOOGLE_API_SERVER_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(apiServerId)
            .build()

        return GoogleSignIn.getClient(context, gso).signInIntent
    }

    fun handleIntentResult(result: ActivityResult, onSuccess: (GoogleSignInAccount) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        onSuccess(task.result)
    }

    fun onDispose() {
        _context = null
    }
}