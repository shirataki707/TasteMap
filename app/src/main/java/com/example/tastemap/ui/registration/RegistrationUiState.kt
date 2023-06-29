package com.example.tastemap.ui.registration

data class RegistrationUiState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val smokingPrioritiesIndex: Int = 0,
    val reviewsPrioritiesIndex: Int = 0,
    val isSignUpButtonEnabled: Boolean = false,
    val event: Event = Event.Idle
) {
    sealed class Event {
        object Idle : Event()
        object Loading : Event()
        object RegisterSuccess : Event()
        data class RegisterFailure(val error: String) : Event()
    }
}
