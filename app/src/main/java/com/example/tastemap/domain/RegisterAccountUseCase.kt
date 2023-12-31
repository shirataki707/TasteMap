package com.example.tastemap.domain

import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.FirestoreRepository
import javax.inject.Inject

class RegisterAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) {

    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String,
        userPreferences: UserPreferences,
        onRegisterSuccess: () -> Unit,
        onRegisterFailure: (String) -> Unit
    ) {
        // アカウントの新規登録に成功後，好み情報を登録(registerUserDetail)
        authRepository.createAccount(
            email = email,
            password = password,
            onSuccess = {
                    registerUserDetail(userName, userPreferences, onRegisterSuccess, onRegisterFailure)},
            onFailure = { e ->
                // [NOTE] メッセージが英語なので，エラーコードに応じて日本語を返す方がベター
                onRegisterFailure(e.localizedMessage)
            }
        )
    }

    // [WARNING] ほんとはsuspendにしてcoroutineScopeで実行したいけど，なぜかうまくいかない
    // 好み情報の登録後，登録がすべて完了とし，画面遷移等のCallBackを実行
    private fun registerUserDetail(
        userName: String,
        userPreferences: UserPreferences,
        onRegisterSuccess: () -> Unit,
        onRegisterFailure: (String) -> Unit
    ) {
        firestoreRepository.addUserDetails(
            userName = userName,
            userPreferences = userPreferences,
            onSuccess = { onRegisterSuccess() },
            onFailure = { e ->
                // [NOTE] メッセージが英語なので，エラーコードに応じて日本語を返す方がベター
                onRegisterFailure(e.localizedMessage)
            }
        )
    }
}
