package com.example.tastemap.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.FirestoreRepository
import com.example.tastemap.domain.RegisterAccountUseCase
import com.example.tastemap.ui.signin.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
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

    val reviewPriorities = listOf(
        "好みはない",
        "星の数を重視",
        "レビュー数を重視"
    )

    val smokingPriorities = listOf(
        "好みはない",
        "禁煙席のみ",
        "喫煙席のみ"
    )

    fun registerAccount(onRegisterSuccess: () -> Unit) {
        _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Loading)
        val userPreferences = UserPreferences(
            reviewPriorities = uiState.value.reviewsPrioritiesIndex,
            smokingPriorities = uiState.value.smokingPrioritiesIndex
        )
        val onRegisterSuccess = {
            _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Success)
            onRegisterSuccess()
        }
        val onRegisterFailure: (String) -> Unit = { error ->
            _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Failure(error))
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

    val dismissError: () -> Unit = {
        _uiState.value = uiState.value.copy(event = RegistrationUiState.Event.Idle)
    }
}