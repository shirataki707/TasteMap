package com.example.tastemap.domain

import com.example.tastemap.data.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, onSignInSuccess: () -> Unit) {
        // [TODO] email, password null or empty check
        authRepository.signIn(
            email,
            password,
            onSuccess = {
                Timber.d("signIn success")
                onSignInSuccess() },
            onFailure = { Timber.d("signIn failure")}
        )
    }
}