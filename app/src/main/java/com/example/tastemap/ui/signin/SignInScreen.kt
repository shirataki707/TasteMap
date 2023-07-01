package com.example.tastemap.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tastemap.R
import com.example.tastemap.ui.components.EmailTextField
import com.example.tastemap.ui.components.ErrorDialog
import com.example.tastemap.ui.components.FullScreenAnimationLoading
import com.example.tastemap.ui.components.PasswordTextField
import com.example.tastemap.ui.components.SuccessAnimation
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(),
    onRegisterButtonClicked: () -> Unit,
    onSignInButtonClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var showSuccessMessage by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // [NOTE] 基本的にuiEventがIdle時以外は入力を受け付けない

            SignInIcon()

            // メールアドレスの入力欄
            EmailTextField(
                email = uiState.email,
                onEmailChange = { email -> viewModel.updateEmail(email) },
                enabled = uiState.event is SignInUiState.Event.Idle,
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // パスワードの入力欄
            PasswordTextField(
                password = uiState.password,
                onPasswordChange = { password -> viewModel.updatePassword(password) },
                enabled = uiState.event is SignInUiState.Event.Idle,
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // ログインボタン
            Button(
                onClick = { viewModel.signIn() },
                modifier = modifier
                    .widthIn(min = dimensionResource(id = R.dimen.width_medium))
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                enabled = uiState.isSignInButtonEnabled
                        && uiState.event is SignInUiState.Event.Idle
            ) {
                Text(stringResource(id = R.string.signin))
            }

        }

        // 新規登録画面への遷移ボタン
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = onRegisterButtonClicked,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                enabled = uiState.event is SignInUiState.Event.Idle
            ) {
                Text(stringResource(id = R.string.to_registration))
            }
        }

        // uiイベントに応じた画面を描画
        when (val event = uiState.event) {
            is SignInUiState.Event.Loading -> { FullScreenAnimationLoading() }
            is SignInUiState.Event.SignInFailure -> {
                ErrorDialog(
                    stringResource(id = R.string.auth_error),
                    event.error,
                    viewModel.dismissDialog
                )
            }
            // 1秒間だけログイン成功のアイコンを表示
            is SignInUiState.Event.SignInSuccess -> {
                LaunchedEffect(Unit) {
                        showSuccessMessage = true
                        delay(2000) // 2秒間表示する
                        showSuccessMessage = false
                        onSignInButtonClicked()
                }
            }
            else -> {}
        }

        if (showSuccessMessage) {
            SuccessAnimation()
        }
    }
}

@Composable
fun SignInIcon(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.singin_icon))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier.fillMaxHeight(0.3f)
    )
}
