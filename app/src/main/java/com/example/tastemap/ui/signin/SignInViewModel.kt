package com.example.tastemap.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState get() = _uiState.asStateFlow()

    fun signIn() {
        _uiState.value = uiState.value.copy(event = SignInUiState.Event.Loading)

        // SignInが正常に完了した場合，onSignInButtonClickedを呼び出し，画面遷移をする
        val onSignInSuccess = {
            _uiState.value = uiState.value.copy(event = SignInUiState.Event.SignInSuccess)
        }

        val onSignInFailure: (String) -> Unit = { error ->
            _uiState.value = uiState.value.copy(event = SignInUiState.Event.SignInFailure(error))
        }

        viewModelScope.launch {
            signInUseCase(
                uiState.value.email,
                uiState.value.password,
                onSignInSuccess,
                onSignInFailure
            )
        }
    }

    fun updateEmail(newEmail: String) {
        _uiState.value = uiState.value.copy(email = newEmail)
        checkButtonState()
    }

    fun updatePassword(newPassword: String) {
        _uiState.value = uiState.value.copy(password = newPassword)
        checkButtonState()
    }

    // メールアドレスとパスワードの両方が空ではない場合だけ，ログインボタンを有効にする
    private fun checkButtonState() {
        _uiState.value = uiState.value.copy(
            isSignInButtonEnabled = uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank()
        )
    }

    val dismissDialog: () -> Unit = {
        _uiState.value = uiState.value.copy(event = SignInUiState.Event.Idle)
    }
}
