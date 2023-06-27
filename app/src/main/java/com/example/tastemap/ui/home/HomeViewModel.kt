package com.example.tastemap.ui.home

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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val searchRestaurantsUseCase: SearchRestaurantsUseCase,
    private val fetchUserDetailsUseCase: FetchUserDetailsUseCase
) : ViewModel() {

    fun searchRestaurants(
        request: HotPepperApiRequest,
        isSortSelected: Boolean,
        onSuccess: (List<Restaurant>) -> Unit
    ) {
        viewModelScope.launch {
            searchRestaurantsUseCase(this, request, isSortSelected, onSuccess)
        }
    }
    fun signOut() {
        viewModelScope.launch { signOutUseCase() }
    }

    fun fetchUserDetails(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            fetchUserDetailsUseCase(onSuccess)
        }
    }

}