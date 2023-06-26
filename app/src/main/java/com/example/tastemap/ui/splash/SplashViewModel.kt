package com.example.tastemap.ui.splash

import androidx.lifecycle.ViewModel
import com.example.tastemap.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun isUserLoggedIn() = authRepository.isUserLoggedIn()
}