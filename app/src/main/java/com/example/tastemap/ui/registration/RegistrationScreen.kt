package com.example.tastemap.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tastemap.R
import com.example.tastemap.ui.components.DropdownList
import com.example.tastemap.ui.components.EmailTextField
import com.example.tastemap.ui.components.ErrorDialog
import com.example.tastemap.ui.components.FullScreenAnimationLoading
import com.example.tastemap.ui.components.PasswordTextField
import com.example.tastemap.ui.components.SuccessAnimation
import kotlinx.coroutines.delay

// [TODO] 架空のメールアドレスで登録できるので，メール認証をする

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(),
    onPopBackButtonClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var showSuccessMessage by remember { mutableStateOf(false) }
    var showEmailValidDialog by remember { mutableStateOf(false) }

    val reviewPriorities = stringArrayResource(id = R.array.reviewPriorities).toList()
    val smokingPriorities = stringArrayResource(id = R.array.smokingPriorities).toList()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // [NOTE] 基本的にuiEventがIdle時以外は入力を受け付けない

            // ユーザ名の入力欄
            // [NOTE] modifierのpaddingとかの設定の順番でレイアウトが変わるため注意
            @OptIn(ExperimentalMaterial3Api::class)
            TextField(
                value = uiState.userName,
                onValueChange = { userName -> viewModel.updateUserName(userName) },
                label = { Text(stringResource(id = R.string.enter_username)) },
                enabled = uiState.event is RegistrationUiState.Event.Idle,
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .widthIn(min = dimensionResource(id = R.dimen.width_medium))
            )

            // メールアドレスの入力欄
            EmailTextField(
                email = uiState.email,
                onEmailChange = { email -> viewModel.updateEmail(email) },
                enabled = uiState.event is RegistrationUiState.Event.Idle,
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // パスワードの入力欄
            PasswordTextField(
                password = uiState.password,
                onPasswordChange = { password -> viewModel.updatePassword(password) },
                enabled = uiState.event is RegistrationUiState.Event.Idle,
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // 飲食店評価の好みを選ぶドロップダウンリスト
            DropdownList(
                items = reviewPriorities,
                selectedIndex = uiState.reviewsPrioritiesIndex,
                onSelectedChange = viewModel.updateReviewPrioritiesIndex,
                caption = stringResource(id = R.string.review_priority),
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // 喫煙・禁煙席の好みを選ぶドロップダウンリスト
            DropdownList(
                items = smokingPriorities,
                selectedIndex = uiState.smokingPrioritiesIndex,
                onSelectedChange = viewModel.updateSmokingPrioritiesIndex,
                caption = stringResource(id = R.string.smoking_priority),
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

            // 登録ボタン
            Button(
                onClick = { viewModel.registerAccount() },
                modifier = modifier
                    .widthIn(min = dimensionResource(id = R.dimen.width_medium))
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                enabled = uiState.isSignUpButtonEnabled
                        && uiState.event is RegistrationUiState.Event.Idle
            ) {
                Text(stringResource(id = R.string.registration))
            }
        }

        // ログイン画面への遷移ボタン
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = onPopBackButtonClicked,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                enabled = uiState.event is RegistrationUiState.Event.Idle
            ) {
                Text(stringResource(id = R.string.back_signin))
            }
        }

        // uiイベントに応じた画面を描画
        when (val event = uiState.event) {
            is RegistrationUiState.Event.Loading -> { FullScreenAnimationLoading() }
            is RegistrationUiState.Event.RegisterFailure -> {
                ErrorDialog(
                    stringResource(id = R.string.auth_error),
                    event.error,
                    viewModel.dismissDialog
                )
            }
            // 1秒間だけログイン成功のアイコンを表示
            is RegistrationUiState.Event.RegisterSuccess -> {
                LaunchedEffect(Unit) {
                    showSuccessMessage = true
                    delay(2000) // 2秒間表示する
                    showSuccessMessage = false
                    showEmailValidDialog = true
                }
                if (showEmailValidDialog) {
                    ValidEmailSendDialog(
                        titleMessage = "登録したアドレスにメールを送信しました",
                        message = "認証をしてください",
                        onDismissRequest = { viewModel.dismissEmailValidDialog(onPopBackButtonClicked) }
                    )
                }
            }
            else -> {}
        }
        if (showSuccessMessage) {
            SuccessAnimation()
        }
    }
}

// 認証メール送信ダイアログ
@Composable
fun ValidEmailSendDialog(
    titleMessage: String,
    message: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = titleMessage) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = "認証しました")
            }
        }
    )
}
