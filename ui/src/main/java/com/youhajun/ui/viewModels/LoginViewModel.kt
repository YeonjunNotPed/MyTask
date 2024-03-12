package com.youhajun.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.model.OAuthToken
import com.youhajun.domain.model.StatusCode
import com.youhajun.domain.model.UiStateErrorVo
import com.youhajun.domain.model.enums.SocialLoginType
import com.youhajun.domain.model.inspectUiState
import com.youhajun.domain.model.vo.LoginRequestVo
import com.youhajun.domain.model.vo.SocialLoginRequestVo
import com.youhajun.domain.usecase.sign.PostLoginUseCase
import com.youhajun.domain.usecase.sign.PostSocialLoginUseCase
import com.youhajun.ui.models.sideEffects.LoginSideEffect
import com.youhajun.ui.models.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


interface LoginIntent {
    fun onClickForgotPassword()
    fun onClickLogin()
    fun onClickSignUp()
    fun onClickGoogleLogin()
    fun onSuccessGoogleLogin(account:GoogleSignInAccount)
    fun onClickKakaoLogin()
    fun onSuccessKakaoLogin(oAuthToken: OAuthToken)
    fun onChangedEmail(email:String)
    fun onChangedPassword(password:String)
    fun onClickHeaderBackIcon()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLogin: PostLoginUseCase,
    private val postSocialLoginUseCase: PostSocialLoginUseCase
) : ContainerHost<LoginState, LoginSideEffect>, ViewModel(), LoginIntent {

    private val _emailStateOf: MutableState<String> = mutableStateOf("")
    private val _passwordStateOf: MutableState<String> = mutableStateOf("")
    val emailStateOf: State<String> = _emailStateOf
    val passwordStateOf: State<String> = _passwordStateOf

    override val container: Container<LoginState, LoginSideEffect> = container(LoginState())
    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(LoginSideEffect.Navigation.NavigateUp) }
    }

    override fun onChangedEmail(email: String) {
        _emailStateOf.value = email
    }

    override fun onChangedPassword(password: String) {
        _passwordStateOf.value = password
    }

    override fun onClickForgotPassword() {

    }

    override fun onClickLogin() {
        viewModelScope.launch {
            val requestVo = LoginRequestVo(_emailStateOf.value, passwordStateOf.value)
            postLogin(requestVo).collect {
                it.inspectUiState(
                    onError = ::onErrorLogin,
                    onSuccess = { onSuccessLogin() }
                )
            }
        }
    }

    private fun onSuccessLogin() = intent {
        postSideEffect(LoginSideEffect.Toast("로그인 성공"))
    }

    private fun onErrorLogin(error: UiStateErrorVo<Unit>) = intent {
        when(error.code) {
            StatusCode.NO_ACCOUNT_ERROR -> postSideEffect(LoginSideEffect.Toast("계정 없음"))
            else -> postSideEffect(LoginSideEffect.Toast("로그인 실패"))
        }
    }

    override fun onClickSignUp() {

    }

    override fun onClickGoogleLogin() {
        intent {
            postSideEffect(LoginSideEffect.GoogleLoginLaunch)
        }
    }

    override fun onSuccessGoogleLogin(account: GoogleSignInAccount) {
        account.serverAuthCode?.let {
            val request = SocialLoginRequestVo(SocialLoginType.GOOGLE, authCode = it)
            socialLogin(request)
        }
    }

    override fun onClickKakaoLogin() {
        intent {
            postSideEffect(LoginSideEffect.KakaoLoginLaunch)
        }
    }

    override fun onSuccessKakaoLogin(oAuthToken: OAuthToken) {
        val request = SocialLoginRequestVo(SocialLoginType.KAKAO, accessToken = oAuthToken.accessToken)
        socialLogin(request)
    }

    private fun socialLogin(request: SocialLoginRequestVo) {
        viewModelScope.launch {
            postSocialLoginUseCase(request).collect {
                it.inspectUiState(
                    onError = ::onErrorLogin,
                    onSuccess = { onSuccessLogin() }
                )
            }
        }
    }
}