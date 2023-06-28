package com.example.tastemap.ui.home

import com.example.tastemap.data.model.Restaurant

data class HomeUiState(
    val userName: String = "",
    val restaurants: List<Restaurant> = emptyList(),
    val event: Event = Event.Idle
) {
    sealed class Event {
        object Idle : Event()
        object Loading : Event()
        object Success : Event()
        data class Failure(val error: String) : Event()
    }
}
