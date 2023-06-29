package com.example.tastemap.ui.home

import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.UserPreferences

data class HomeUiState(
    val userName: String = "",
    val userPreferences: UserPreferences = UserPreferences(),
    val restaurants: List<Restaurant> = emptyList(),
    val searchRangeIndex: Int = 2,
    val isSortOptionSelected: Boolean = true,
    val keyword: String = "",
    val genreIndex: Int = 0,
    val event: Event = Event.Idle
) {
    sealed class Event {
        object Idle : Event()
        object Loading : Event()
        object Success : Event()
        data class Failure(val error: String) : Event()
    }
}
