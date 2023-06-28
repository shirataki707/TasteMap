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
import com.example.tastemap.data.model.UserPreferences
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

    // rangeは5段階．1. 300m, 2. 500m, 3. 1000m(default), 4. 2000m, 5. 3000m
    val searchRanges = listOf(
        "300m",
        "500m",
        "1km",
        "2km",
        "3km"
    )

    val genres = listOf(
        "指定なし",
        "居酒屋",
        "ダイニングバー・バル",
        "創作料理",
        "和食",
        "洋食",
        "イタリアン・フレンチ",
        "中華",
        "焼肉・ホルモン",
        "韓国料理",
        "アジア・エスニック料理",
        "各国料理",
        "カラオケ・パーティ",
        "バー・カクテル",
        "ラーメン",
        "お好み焼き・もんじゃ",
        "カフェ・スイーツ",
        "その他グルメ"
    )

    val genresCode = listOf(
        "",
        "G001",
        "G002",
        "G003",
        "G004",
        "G005",
        "G006",
        "G007",
        "G008",
        "G017",
        "G009",
        "G010",
        "G011",
        "G012",
        "G013",
        "G016",
        "G014",
        "G015"
    )

    init {
        viewModelScope.launch {
            fetchUserDetails()
        }
    }

    fun searchRestaurants() {
        _uiState.value = uiState.value.copy(event = HomeUiState.Event.Loading)

        val request = HotPepperApiRequest(
            lat = 33.652294,
            lng = 130.672144,
            range = uiState.value.searchRangeIndex + 1, // apiのrangeは1から始まるが，uiStateは0から始まるため
            keyword = uiState.value.keyword,
            genre = genresCode[uiState.value.genreIndex]
        )

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
    fun signOut() {
        viewModelScope.launch { signOutUseCase() }
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
            Timber.e("fetchuserdetail error")
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