package com.example.tastemap.domain

import com.example.tastemap.data.repository.AuthRepository
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
        authRepository.signIn(
            email,
            password,
            onSuccess = {
                onSignInSuccess() },
            onFailure = { e ->
                // [NOTE] メッセージが英語なので，エラーコードに応じて日本語を返す方がベター
                onSignInFailure(e.localizedMessage)
            }
        )
    }
}
