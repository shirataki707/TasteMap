package com.example.tastemap.ui.profile

import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.UserPreferences

data class ProfileUiState(
    val userName: String = "",
    val userPreferences: UserPreferences = UserPreferences(),
    val event: Event = Event.Idle
) {
    sealed class Event {
        object Idle : Event()
        object Loading : Event()
        object Success : Event()
        data class Failure(val error: String) : Event()
    }
}
