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
        object SignInSuccess : Event()
        data class SignInFailure(val error: String) : Event()
    }
}
