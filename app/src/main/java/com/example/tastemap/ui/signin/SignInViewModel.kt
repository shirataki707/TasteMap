package com.example.tastemap.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.AuthRepository_Factory
import com.example.tastemap.domain.SignInUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    fun signIn(email: String, password: String, onSignInSuccess: () -> Unit) {
        // [TODO] email, password null or empty check
        viewModelScope.launch { signInUseCase(email, password, onSignInSuccess) }
    }
}