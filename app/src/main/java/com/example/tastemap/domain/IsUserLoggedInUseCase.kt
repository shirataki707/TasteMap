package com.example.tastemap.domain

import com.example.tastemap.data.repository.AuthRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.isUserLoggedIn()
}
