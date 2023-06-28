package com.example.tastemap.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.util.rangeTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.model.Location
import com.example.tastemap.data.model.PlaceDetailResult
import com.example.tastemap.data.model.PlaceId
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.Shop
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.FirestoreRepository
import com.example.tastemap.data.repository.HotPepperApiRepository
import com.example.tastemap.data.repository.PlacesApiRepository
import com.example.tastemap.domain.FetchUserDetailsUseCase
import com.example.tastemap.domain.SearchRestaurantsUseCase
import com.example.tastemap.domain.SignOutUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val searchRestaurantsUseCase: SearchRestaurantsUseCase,
    private val fetchUserDetailsUseCase: FetchUserDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchUserDetails()
        }
    }

    fun searchRestaurants(
        request: HotPepperApiRequest,
        isSortSelected: Boolean,
    ) {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Loading)
        val onSuccess: (List<Restaurant>) -> Unit = { restaurants ->
            _uiState.value = uiState.value.copy(restaurants = restaurants)
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Success)
        }
        val onFailure: (String) -> Unit =  { error ->
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Failure(error))
        }
        viewModelScope.launch {
            searchRestaurantsUseCase(this, request, isSortSelected, onSuccess, onFailure)
        }
    }
    fun signOut() {
        viewModelScope.launch { signOutUseCase() }
    }

    private fun fetchUserDetails() {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Loading)
        val onSuccess: (String) -> Unit = { name ->
            _uiState.value = uiState.value.copy(userName = name)
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Success)
            Timber.d("$name")
        }
        val onFailure: (String) -> Unit =  { error ->
            _uiState.value = uiState.value.copy(event = HomeUiState.Event.Failure(error))
            Timber.e("fetchuserdetail error")
        }
        viewModelScope.launch {
            fetchUserDetailsUseCase(onSuccess, onFailure)
        }
    }

    val dismissError: () -> Unit = {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Idle)
    }

}