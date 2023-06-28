package com.example.tastemap.ui.signin

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isSignInButtonEnabled: Boolean = false,
    val event: Event = Event.Idle
) {
    sealed class Event {
        object Idle : Event()
        object Loading : Event()
        object Success : Event()
        data class Failure(val error: String) : Event()
    }
}