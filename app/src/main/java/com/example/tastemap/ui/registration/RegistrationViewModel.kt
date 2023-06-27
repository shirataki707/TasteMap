package com.example.tastemap.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.FirestoreRepository
import com.example.tastemap.domain.RegisterAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerAccountUseCase: RegisterAccountUseCase
) : ViewModel() {

    fun RegisterAccount(
        userName: String,
        email: String,
        password: String,
        isSmoker: Boolean,
        onRegisterSuccess: () -> Unit
    ) {
        val userPreferences = UserPreferences(
            genre = "",
            isSmoker = isSmoker
        )
        viewModelScope.launch {
            registerAccountUseCase(
                userName,
                email,
                password,
                userPreferences,
                onRegisterSuccess
            )
        }
    }
}