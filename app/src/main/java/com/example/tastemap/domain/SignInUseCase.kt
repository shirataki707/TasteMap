package com.example.tastemap.domain

import com.example.tastemap.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import timber.log.Timber
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        // [TODO] email, password null or empty check
        authRepository.signIn(
            email,
            password,
            onSuccess = {
                Timber.d("signIn success")
                onSignInSuccess() },
            onFailure = { e ->
                Timber.d("signIn failure")
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "無効な認証情報です。${e.message}"
                    else -> "ログインに失敗しました。${e.message}"
                }
                onSignInFailure(errorMessage)
            }
        )
    }
}