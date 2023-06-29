package com.example.tastemap.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.domain.FetchUserDetailsUseCase
import com.example.tastemap.domain.SignOutUseCase
import com.example.tastemap.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchUserDetailsUseCase: FetchUserDetailsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchUserDetails()
        }
    }

    private fun fetchUserDetails() {
        _uiState.value = uiState.value.copy(event = ProfileUiState.Event.Loading)
        val onSuccess: (String, UserPreferences) -> Unit = { name, preferences ->
            _uiState.value = uiState.value.copy(userName = name)
            _uiState.value = uiState.value.copy(userPreferences = preferences)
            _uiState.value = uiState.value.copy(event = ProfileUiState.Event.Success)
            Timber.d("$name, $preferences")
        }
        val onFailure: (String) -> Unit =  { error ->
            _uiState.value = uiState.value.copy(event = ProfileUiState.Event.Failure(error))
            Timber.e("fetchuserdetail error")
        }
        viewModelScope.launch {
            fetchUserDetailsUseCase(onSuccess, onFailure)
        }
    }

    fun signOut() {
        viewModelScope.launch { signOutUseCase() }
    }

}