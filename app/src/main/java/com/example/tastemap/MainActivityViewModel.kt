package com.example.tastemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.repository.HotPepperApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val hotPepperApiRepository: HotPepperApiRepository
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
}