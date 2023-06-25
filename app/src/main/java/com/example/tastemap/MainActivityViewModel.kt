package com.example.tastemap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiDetailResponse
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.model.PlaceDetailResult
import com.example.tastemap.data.model.PlaceId
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.Shop
import com.example.tastemap.data.repository.AuthRepository
import com.example.tastemap.data.repository.HotPepperApiRepository
import com.example.tastemap.data.repository.PlacesApiRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val hotPepperApiRepository: HotPepperApiRepository,
    private val placesApiRepository: PlacesApiRepository,
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
): ViewModel() {

    fun fetchRestaurants(request: HotPepperApiRequest, isSortSelected: Boolean) {
        viewModelScope.launch {
            val shopsDeferred = async { hotPepperApiRepository.fetchShops(request) }
            val shopsResult = shopsDeferred.await()
            var shops: List<Shop> = listOf()
            shopsResult.onSuccess { response ->
                shops = applyPreference(response.results.shop)
            }
            shopsResult.onFailure { Timber.e(it.toString()) }

            Timber.d("shops: $shops")

            if (isSortSelected) {
                val placeIds: MutableList<String> = mutableListOf()
                shops.forEach { shop ->
                    val placeIdRequest = PlacesApiIdRequest(input = shop.name)
                    val placeIdDeferred = async { placesApiRepository.fetchPlaceId(placeIdRequest) }
                    val placeId = placeIdDeferred.await()
                    placeId.onSuccess { response ->
                        val nearestPlaceId = searchNearestId(response.candidates)
                        placeIds.add(nearestPlaceId)
                    }
                    placeId.onFailure { Timber.e(it.toString()) }

                }

                val placeDetails: MutableList<PlaceDetailResult> = mutableListOf()
                placeIds.forEach { placeId ->
                    val placeDetailRequest = PlacesApiDetailRequest(placeId = placeId)
                    val placeDetailDeferred = async { placesApiRepository.fetchPlaceDetail(placeDetailRequest) }
                    val placeDetail = placeDetailDeferred.await()
                    placeDetail.onSuccess { response ->
                        placeDetails.add(response.result)
                    }
                    placeDetail.onFailure { Timber.e(it.toString()) }
                }

                // [TODO] スコアでソートして，5件viewmodelに格納->recompose
                Timber.d("shops: $shops, scores: $placeDetails")
            } else {
                // [TODO] 5件程度にして返す
                val restaurants: List<Restaurant> = listOf()
                // viewmodelのパラメータを変更->recompose
            }

        }
    }

    private fun searchNearestId(candidates: List<PlaceId>): String {
        // [TODO] 最近傍のIDを返す
        return candidates[0].placeId
    }

    private fun applyPreference(shopList: List<Shop>): List<Shop> {
        // [TODO] shopListから，好みを抜き出し，ランダムにソート, 20件程度にして返す(ID, Detailのリクエスト回数削減)
        return shopList
    }

    fun signUp(email: String, password: String) {
        authRepository.createAccount(
            email,
            password,
            onSuccess = {
                Timber.d("signUp success")
                Timber.d("currentUser: ${auth.currentUser}") },
            onFailure = { Timber.d("signUp failure")}
        )
    }

    fun signIn(email: String, password: String) {
        authRepository.signIn(
            email,
            password,
            onSuccess = {
                Timber.d("signIn success")
                Timber.d("currentUser: ${auth.currentUser}")},
            onFailure = { Timber.d("signIn failure")}
        )
    }

    fun signOut() {
        authRepository.signOut()
        Timber.d("currentUser: ${auth.currentUser}")
    }

}