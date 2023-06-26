package com.example.tastemap.ui.home

import androidx.lifecycle.ViewModel
import com.example.tastemap.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    fun signOut() {
        authRepository.signOut()
        Timber.d("currentUser: ${auth.currentUser}")
    }
}