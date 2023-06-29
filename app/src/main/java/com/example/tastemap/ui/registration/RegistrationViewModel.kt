package com.example.tastemap.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.domain.RegisterAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerAccountUseCase: RegisterAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState get() = _uiState.asStateFlow()

    fun registerAccount() {
        _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Loading)

        // 好み情報
        val userPreferences = UserPreferences(
            reviewPriorities = uiState.value.reviewsPrioritiesIndex,
            smokingPriorities = uiState.value.smokingPrioritiesIndex
        )

        // Registerが正常に完了した時は完了メッセージを出して画面遷移
        val onRegisterSuccess = {
            _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.RegisterSuccess)
        }

        // Registerが失敗した時はエラーダイアログを表示
        val onRegisterFailure: (String) -> Unit = { error ->
            _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.RegisterFailure(error))
        }

        viewModelScope.launch {
            registerAccountUseCase(
                uiState.value.userName,
                uiState.value.email,
                uiState.value.password,
                userPreferences,
                onRegisterSuccess,
                onRegisterFailure
            )
        }
    }

    fun updateUserName(newName: String) {
        _uiState.value = uiState.value.copy(userName = newName)
        checkButtonState()
    }

    fun updateEmail(newEmail: String) {
        _uiState.value = uiState.value.copy(email = newEmail)
        checkButtonState()
    }

    fun updatePassword(newPassword: String) {
        _uiState.value = uiState.value.copy(password = newPassword)
        checkButtonState()
    }

    // メールアドレス，パスワード，ユーザ名がすべて空ではない場合だけ，登録ボタンを有効にする
    private fun checkButtonState() {
        _uiState.value = uiState.value.copy(
            isSignUpButtonEnabled = uiState.value.email.isNotBlank()
                    && uiState.value.password.isNotBlank()
                    && uiState.value.userName.isNotBlank()
        )
    }

    val updateSmokingPrioritiesIndex: (Int) -> Unit = { newIndex ->
        _uiState.value = uiState.value.copy(smokingPrioritiesIndex = newIndex)
    }

    val updateReviewPrioritiesIndex: (Int) -> Unit = { newIndex ->
        _uiState.value = uiState.value.copy(reviewsPrioritiesIndex = newIndex)
    }

    val dismissDialog: () -> Unit = {
        _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Idle)
    }
}
