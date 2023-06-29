package com.example.tastemap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.domain.FetchUserDetailsUseCase
import com.example.tastemap.domain.SearchRestaurantsUseCase
import com.example.tastemap.domain.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val searchRestaurantsUseCase: SearchRestaurantsUseCase,
    private val fetchUserDetailsUseCase: FetchUserDetailsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchUserDetails()
        }
    }

    fun searchRestaurants(request: HotPepperApiRequest) {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Loading)

        Timber.d("hotPepperApiRequest: $request")

        val onSuccess: (List<Restaurant>) -> Unit = { restaurants ->
            if (restaurants.isEmpty()) {
                val emptyMessage = "検索結果は0件でした。条件を変えて検索してください。"
                _uiState.value = uiState.value.copy(event = HomeUiState.Event.Failure(emptyMessage))
            } else {
                _uiState.value = uiState.value.copy(event = HomeUiState.Event.Success)
            }
            _uiState.value = uiState.value.copy(restaurants = restaurants)

        }

        val onFailure: (String) -> Unit =  { error ->
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Failure(error))
        }

        viewModelScope.launch {
            searchRestaurantsUseCase(
                this,
                request,
                uiState.value.isSortOptionSelected,
                onSuccess,
                onFailure
            )
        }
    }

    private fun fetchUserDetails() {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Loading)
        val onSuccess: (String, UserPreferences) -> Unit = { name, preferences ->
            _uiState.value = uiState.value.copy(userName = name)
            _uiState.value = uiState.value.copy(userPreferences = preferences)
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Success)
            Timber.d("$name, $preferences")
        }
        val onFailure: (String) -> Unit =  { error ->
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Failure(error))
        }
        viewModelScope.launch {
            fetchUserDetailsUseCase(onSuccess, onFailure)
        }
    }

    val dismissError: () -> Unit = {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Idle)
    }

    val updateSearchRangeIndex: (Int) -> Unit = { newIndex ->
        _uiState.value = uiState.value.copy(searchRangeIndex = newIndex)
    }

    val updateGenreIndex: (Int) -> Unit = { newIndex ->
        _uiState.value = uiState.value.copy(genreIndex = newIndex)
    }

    fun updateKeyword(newKeyword: String) {
        _uiState.value = uiState.value.copy(keyword = newKeyword)
    }

    fun updateIsSortOptionChecked(newBoolean: Boolean) {
        _uiState.value = uiState.value.copy(isSortOptionSelected = newBoolean)
    }
}
