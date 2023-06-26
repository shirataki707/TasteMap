package com.example.tastemap.ui.registration

import androidx.lifecycle.ViewModel
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
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
        authRepository.createAccount(
            email = email,
            password = password,
            onSuccess = { RegisterUserDetail(userName, userPreferences, onRegisterSuccess) },
            onFailure = {}
        )
    }

    private fun RegisterUserDetail(
        userName: String,
        userPreferences: UserPreferences,
        onRegisterSuccess: () -> Unit
    ) {
        firestoreRepository.addUserDetails(
            userName = userName,
            userPreferences = userPreferences,
            onSuccess = { onRegisterSuccess() },
            onFailure = {}
        )
    }
}