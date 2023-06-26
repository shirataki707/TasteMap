package com.example.tastemap.ui.signin

import androidx.lifecycle.ViewModel
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.AuthRepository_Factory
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    fun signIn(email: String, password: String, onSignInSuccess: () -> Unit) {
        // [TODO] email, password null or empty check
        authRepository.signIn(
            email,
            password,
            onSuccess = {
                Timber.d("signIn success")
                Timber.d("currentUser: ${auth.currentUser}")
                onSignInSuccess() },
            onFailure = { Timber.d("signIn failure")}
        )
    }
}