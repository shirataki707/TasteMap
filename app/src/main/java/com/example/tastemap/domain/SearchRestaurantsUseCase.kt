package com.example.tastemap.domain

import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.hotpepper.HotPepperApiResponse
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiDetailResponse
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.api.places.PlacesApiIdResponse
import com.example.tastemap.data.model.Location
import com.example.tastemap.data.model.PlaceDetailResult
import com.example.tastemap.data.model.PlaceId
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.Shop
import com.example.tastemap.data.repository.HotPepperApiRepository
import com.example.tastemap.data.repository.PlacesApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Math.min
import javax.inject.Inject

class SearchRestaurantsUseCase @Inject constructor(
    private val hotPepperApiRepository: HotPepperApiRepository,
    private val placesApiRepository: PlacesApiRepository
) {

    suspend operator fun invoke(
        scope: CoroutineScope,
        request: HotPepperApiRequest,
        isSortSelected: Boolean,
        onSuccess: (List<Restaurant>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            // HotPepperAPIからN件のレストランを検索して好みのものだけ返す
            val shops: List<Shop> = fetchPreferenceShops(scope, request)

            Timber.d("shops: $shops")

            // Googleの星とレビュー数でソートする場合
            // shopsの全ての飲食店のPlaceIDを取得
            val placeIds: List<String> = fetchPlaceIds(scope, shops)

            Timber.d("placeIds: $placeIds")

            // 対応したPlaceIDがないお店は除く
            val (filteredShops, filteredPlaceIds) = filterEmptyIDs(shops, placeIds)

            Timber.d("filteredShops: $filteredShops, filteredPlaceIds: $filteredPlaceIds")

            // すべてのPlaceIDに対して，お店の詳細情報を取得
            val placeDetails: List<PlaceDetailResult> = fetchPlaceDetails(scope, filteredPlaceIds)

            Timber.d("placeDetails: $placeDetails")

            // HotPepperとPlacesのレスポンスからUIで使うお店情報を作成
            val restaurants: List<Restaurant> = createRestaurantsFromResponse(filteredShops, placeDetails)

            Timber.d("restaurants: $restaurants")

            // [TODO] ソート
            if (isSortSelected) {

            }

            onSuccess(restaurants)

            Timber.d("shops: $filteredShops, scores: $placeDetails")

        } catch (e: Exception) {
            onFailure("飲食店情報の取得に失敗しました: ${e.message}")
        }

    }

    private fun filterEmptyIDs(shops: List<Shop>, placeIds: List<String>): Pair<List<Shop>, List<String>> {
        val zipped = shops.zip(placeIds)
        val filtered = zipped.filter { (_, id) -> id.isNotEmpty() }
        return filtered.unzip()
    }

    // HotPepperAPIを用いて，周辺のお店を検索して返す．
    private suspend fun fetchShops(
        scope: CoroutineScope,
        request: HotPepperApiRequest
    ): Result<HotPepperApiResponse> {
        return withContext(scope.coroutineContext) {
            hotPepperApiRepository.fetchShops(
                request
            )
        }
    }

    // fetchShopsで得たお店から好みのお店のみ抽出して返す．
    private suspend fun fetchPreferenceShops(
        scope: CoroutineScope,
        request: HotPepperApiRequest
    ): List<Shop> {
        val shopsResult = fetchShops(scope, request)
        var shops: List<Shop> = listOf()
        shopsResult.onSuccess { response ->
            // 好み情報を反映したレストランのみ抽出
            shops = applyPreference(response.results.shop)
            // シャッフルした後20件程度にして返す．20件に満たない場合はリスト全体
            shops = shops.shuffled().take(20)
        }
        shopsResult.onFailure { Timber.e(it.toString()) }

        return shops
    }

    // 1件のお店に対応するPlaceIDを返す．
    private suspend fun fetchPlaceId(
        scope: CoroutineScope,
        request: PlacesApiIdRequest
    ): Result<PlacesApiIdResponse> {
        // 対応するPlaceIDがないときもある
        return withContext(scope.coroutineContext) {
            placesApiRepository.fetchPlaceId(request)
        }
    }

    // お店のリストに対して，PlaceIDのリストを返す．
    private suspend fun fetchPlaceIds(scope: CoroutineScope, shops: List<Shop>): List<String> {
        val placeIds: MutableList<String> = mutableListOf()
        shops.forEach { shop ->
            val placeIdRequest = PlacesApiIdRequest(input = shop.name)
            val placeId = fetchPlaceId(scope, placeIdRequest)
            placeId.onSuccess { response ->
                // 複数IDが返される場合は，最も近いLocationのIDを返す
                val nearestPlaceId = searchNearestId(response.candidates)
                placeIds.add(nearestPlaceId)
            }
            placeId.onFailure { Timber.e(it.toString()) }
        }
        return placeIds.toList()
    }

    // 1つのPlaceIDに対応するお店の詳細情報を返す．
    private suspend fun fetchPlaceDetail(
        scope: CoroutineScope,
        request: PlacesApiDetailRequest
    ): Result<PlacesApiDetailResponse> {
        return withContext(scope.coroutineContext) {
            placesApiRepository.fetchPlaceDetail(request)
        }
    }

    // PlaceIDのリストに対して，詳細情報のリストを返す．
    private suspend fun fetchPlaceDetails(
        scope: CoroutineScope,
        placeIds: List<String>
    ): List<PlaceDetailResult> {
        val placeDetails: MutableList<PlaceDetailResult> = mutableListOf()
        placeIds.forEach { placeId ->
            val placeDetailRequest = PlacesApiDetailRequest(placeId = placeId)
            val placeDetail = fetchPlaceDetail(scope, placeDetailRequest)
            placeDetail.onSuccess { response ->
                placeDetails.add(response.result)
            }
            placeDetail.onFailure { Timber.e(it.toString()) }
        }
        return placeDetails.toList()
    }

    // HotPepperの結果(shops)とPlaceDetailを用いて，Restaurantを作成
    private fun createRestaurantsFromResponse(
        shops: List<Shop>,
        placeDetails: List<PlaceDetailResult>
    ): List<Restaurant> {
        val restaurants: MutableList<Restaurant> = mutableListOf()
        // [TODO] いったんRestaurantを全て作った後，オプションに応じてソート．その後，5件を返す．
        // とりあえず5件返す
        for (i in (0 until (kotlin.math.min(placeDetails.size, MAX_RESTAURANTS)))) {
            restaurants.add(
                Restaurant(
                    name = shops[i].name,
                    rating = placeDetails[i].rating,
                    userReviews = placeDetails[i].userRatingTotal,
                    location = Location(latitude = shops[i].lat, longitude = shops[i].lng),
                    isOpenNow = placeDetails[i].currentOpeningHours.openNow,
                    weekdayText = placeDetails[i].currentOpeningHours.weekdayText,
                    priceLevel = placeDetails[i].priceLevel,
                    website = placeDetails[i].website
                )
            )
        }
        Timber.d("createRestaurant: $restaurants")
        return restaurants.toList()
    }

    // PlaceIDが複数ある場合に対処するため，最近傍のものを探して返す．
    private fun searchNearestId(candidates: List<PlaceId>): String {
        // [TODO] 最近傍のIDを返す．一旦は０個目を返す
        return candidates[0].placeId
    }

    // 好み情報を反映したリストを返す．
    private fun applyPreference(shopList: List<Shop>): List<Shop> {
        // [TODO] shopListから，好みを抜き出し，ランダムにソート, 20件程度にして返す(ID, Detailのリクエスト回数削減)
        return shopList
    }

    companion object {
        const val MAX_RESTAURANTS = 5
    }
}