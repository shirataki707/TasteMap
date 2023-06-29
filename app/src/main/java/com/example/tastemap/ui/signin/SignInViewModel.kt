package com.example.tastemap.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.AuthRepository_Factory
import com.example.tastemap.domain.SignInUseCase
import com.example.tastemap.ui.home.HomeUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState get() = _uiState.asStateFlow()

    fun signIn(onSignInButtonClicked: () -> Unit) {
        _uiState.value = uiState.value.copy(event = SignInUiState.Event.Loading)
        val onSignInSuccess = {
            _uiState.value = uiState.value.copy(event = SignInUiState.Event.Success)
            onSignInButtonClicked()
        }
        val onSignInFailure: (String) -> Unit = { error ->
            _uiState.value = uiState.value.copy(event = SignInUiState.Event.Failure(error))
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

    private fun checkButtonState() {
        _uiState.value = uiState.value.copy(
            isSignInButtonEnabled = uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank()
        )
    }

    val dismissError: () -> Unit = {
        _uiState.value = uiState.value.copy(event = SignInUiState.Event.Idle)
    }
}