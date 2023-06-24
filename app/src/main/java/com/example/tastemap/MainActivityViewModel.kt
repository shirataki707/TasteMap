package com.example.tastemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.repository.HotPepperApiRepository
import com.example.tastemap.data.repository.PlacesApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val hotPepperApiRepository: HotPepperApiRepository,
    private val placesApiRepository: PlacesApiRepository
): ViewModel() {

    fun fetchShops(request: HotPepperApiRequest) {
        viewModelScope.launch {
            val response = hotPepperApiRepository.fetchShops(request)
            response.onSuccess { response ->
                Timber.d(response.toString())
            }
            response.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun fetchPlaceId(request: PlacesApiIdRequest) {
        viewModelScope.launch {
            val response = placesApiRepository.fetchPlaceId(request)
            response.onSuccess { response ->
                Timber.d(response.toString())
            }
            response.onFailure {
                Timber.e(it.toString())
            }
        }
    }

    fun fetchPlaceDetail(request: PlacesApiDetailRequest) {
        viewModelScope.launch {
            val response = placesApiRepository.fetchPlaceDetail(request)
            response.onSuccess { response ->
                Timber.d(response.toString())
            }
            response.onFailure {
                Timber.e(it.toString())
            }
        }
    }
}