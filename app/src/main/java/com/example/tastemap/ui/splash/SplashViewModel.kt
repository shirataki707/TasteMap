package com.example.tastemap.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.domain.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {
//    private val _isLoggedIn = MutableStateFlow(false)
//    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn
//
//    init {
//        checkUserLoginStatus()
//    }

    fun checkUserLoginStatus(): Boolean {
//        viewModelScope.launch {
//            _isLoggedIn.value = isUserLoggedInUseCase()
//        }
        return isUserLoggedInUseCase()
    }
}