package com.youhajun.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.MyTaskHeader
import com.youhajun.ui.components.input.TopTitleInput
import com.youhajun.ui.components.social.GoogleLoginButton
import com.youhajun.ui.components.social.KakaoLoginButton
import com.youhajun.model_ui.sideEffects.LoginSideEffect
import com.youhajun.ui.utils.GoogleLoginUtil
import com.youhajun.ui.utils.KakaoLoginUtil
import com.youhajun.ui.viewModels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (LoginSideEffect.Navigation) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val googleLoginUtil = remember { GoogleLoginUtil(context) }
    val kakaoLoginUtil = remember { KakaoLoginUtil(context) }
    val googleLoginLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                googleLoginUtil.handleIntentResult(result) {
                    viewModel.onSuccessGoogleLogin(it)
                }
            }
        }

    DisposableEffect(Unit) {
        onDispose {
            googleLoginUtil.onDispose()
            kakaoLoginUtil.onDispose()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is LoginSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT)
                    .show()

                LoginSideEffect.GoogleLoginLaunch -> {
                    val googleLoginIntent = googleLoginUtil.getGoogleLoginIntent()
                    googleLoginLauncher.launch(googleLoginIntent)
                }

                LoginSideEffect.KakaoLoginLaunch -> kakaoLoginUtil.kakaoLogin {
                    viewModel.onSuccessKakaoLogin(it)
                }

                LoginSideEffect.HideKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }

                is LoginSideEffect.Navigation -> onNavigate.invoke(it)
            }
        }

        googleLoginUtil.checkAlreadyGoogleLogin {
            viewModel.onSuccessGoogleLogin(it)
        }
    }

    Column(modifier = Modifier
        .background(colorResource(id = R.color.color_161616))
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {

        MyTaskHeader(title = stringResource(id = R.string.header_title_login), onClickLeftIcon = viewModel::onClickHeaderBackIcon)

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.W800,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            TopTitleInput(
                R.string.login_email_title,
                R.string.login_email_hint,
                KeyboardType.Text, { viewModel.emailStateOf.value },
                viewModel::onChangedEmail
            )

            Spacer(modifier = Modifier.height(20.dp))

            TopTitleInput(
                R.string.login_password_title,
                R.string.login_password_hint,
                KeyboardType.Password, { viewModel.passwordStateOf.value },
                viewModel::onChangedPassword
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = viewModel::onClickForgotPassword),
                text = stringResource(id = R.string.login_forgot_password),
                fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.W400,
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::onClickLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.color_292929),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = R.color.color_292929),
                    disabledContentColor = Color.White,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login_sign_in),
                    fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.W600
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = viewModel::onClickSignUp),
                text = stringResource(id = R.string.login_sign_up),
                fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            KakaoLoginButton(viewModel::onClickKakaoLogin)

            Spacer(modifier = Modifier.height(20.dp))

            GoogleLoginButton(viewModel::onClickGoogleLogin)
        }
    }
}