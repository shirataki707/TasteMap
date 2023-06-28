package com.example.tastemap.ui.profile

import androidx.lifecycle.ViewModel
import com.example.tastemap.domain.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

}