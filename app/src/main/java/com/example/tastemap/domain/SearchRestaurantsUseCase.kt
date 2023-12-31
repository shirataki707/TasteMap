package com.example.tastemap.domain

import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.hotpepper.HotPepperApiResponse
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiDetailResponse
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.api.places.PlacesApiIdResponse
import com.example.tastemap.data.model.Location
import com.example.tastemap.data.model.Photo
import com.example.tastemap.data.model.PlaceDetailResult
import com.example.tastemap.data.model.PlaceId
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.data.model.Shop
import com.example.tastemap.data.model.UserPreferences
import com.example.tastemap.data.repository.HotPepperApiRepository
import com.example.tastemap.data.repository.PlacesApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SearchRestaurantsUseCase @Inject constructor(
    private val hotPepperApiRepository: HotPepperApiRepository,
    private val placesApiRepository: PlacesApiRepository
) {

    suspend operator fun invoke(
        scope: CoroutineScope,
        request: HotPepperApiRequest,
        isSortSelected: Boolean,
        userPreferences: UserPreferences,
        onSuccess: (List<Restaurant>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            // HotPepperAPIからN件のレストランを検索して好みのものだけ返す
            // [TODO] 好み(userPreferences)を反映
            val shops: List<Shop> = fetchPreferenceShops(scope, request, userPreferences)

            Timber.d("shops: $shops")

            // Googleの星とレビュー数でソートする場合
            // shopsの全ての飲食店のPlaceIDを取得
            // [TODO] 最近傍のPlaceIDを返す
            val placeIds: List<String> = fetchPlaceIds(scope, shops)

            Timber.d("placeIds: $placeIds")

            // 対応したPlaceIDがないお店は除く
            val (filteredShops, filteredPlaceIds) = filterEmptyIDs(shops, placeIds)

            Timber.d("filteredShops: $filteredShops, filteredPlaceIds: $filteredPlaceIds")

            // すべてのPlaceIDに対して，お店の詳細情報を取得
            val placeDetails: List<PlaceDetailResult> = fetchPlaceDetails(scope, filteredPlaceIds)

            Timber.d("placeDetails: $placeDetails")

            // HotPepperとPlacesのレスポンスからUIで使うお店情報を作成
            // [TODO] UIで表示するデータを作ろう
            var restaurants: List<Restaurant> = createRestaurantsFromResponse(filteredShops, placeDetails)

            Timber.d("restaurants: $restaurants")

            // [TODO] 星とレビュー数などからソートしよう
            if (isSortSelected) {
                restaurants = sortRestaurants(restaurants)
            }

            onSuccess(restaurants)

            Timber.d("shops: $filteredShops, scores: $placeDetails")

        } catch (e: Exception) {
            onFailure("飲食店情報の取得に失敗しました: ${e.message}")
        }

    }

    // 空のPlaceIDがあるお店を除外
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
        request: HotPepperApiRequest,
        userPreferences: UserPreferences
    ): List<Shop> {
        val shopsResult = fetchShops(scope, request)
        var shops: List<Shop> = listOf()
        shopsResult.onSuccess { response ->
            // 好み情報を反映したレストランのみ抽出
            shops = applyPreference(response.results.shop, userPreferences)
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
                Timber.d("detail response: ${response.result}")
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
                    name = shops[i].name,   // [NOTE] shops(hotPepper)とplaceDetails(google)のどちらかの名前
                    rating = placeDetails[i].rating,
                    userReviews = placeDetails[i].userRatingTotal,
                    location = Location(latitude = shops[i].lat, longitude = shops[i].lng),
                    isOpenNow = placeDetails[i].currentOpeningHours?.openNow,
                    weekdayText = placeDetails[i].currentOpeningHours?.weekdayText,
                    priceLevel = placeDetails[i].priceLevel,
                    website = placeDetails[i].website,
                    image = getRestaurantImage(shops[i].photo)
                )
            )
        }
        Timber.d("createRestaurant: $restaurants")
        return restaurants.toList()
    }

    // PlaceIDが複数ある場合に対処するため，最近傍のものを探して返す．nullだった場合は""を返して後でフィルタリング
    private fun searchNearestId(candidates: List<PlaceId>?): String {
        // [TODO] 最近傍のIDを返す．一旦は０個目を返す
        Timber.d("raw placeId: $candidates")
        return if (candidates.isNullOrEmpty()) {
            ""
        } else {
            candidates[0].placeId
        }
    }

    // 好み情報を反映したリストを返す．
    private fun applyPreference(shopList: List<Shop>, userPreferences: UserPreferences): List<Shop> {
        // [TODO] shopListから好みを抜き出し, 20件程度にして返す(ID, Detailのリクエスト回数削減)
        return shopList
    }

    // hotPepperの結果から画像を探す，PC(l), Mobile(l), PC(m), PC(s), Mobile(s)の優先順
    private fun getRestaurantImage(photo: Photo?): String? {
        return photo?.pc?.l.takeIf { !it.isNullOrEmpty() }
            ?: photo?.mobile?.l.takeIf { !it.isNullOrEmpty() }
            ?: photo?.pc?.m.takeIf { !it.isNullOrEmpty() }
            ?: photo?.pc?.s.takeIf { !it.isNullOrEmpty() }
            ?: photo?.mobile?.s.takeIf { !it.isNullOrEmpty() }
    }

    // [TODO] 独自のソートロジックを適用
    private fun sortRestaurants(restaurants: List<Restaurant>): List<Restaurant> {
        return restaurants
    }

    companion object {
        const val MAX_RESTAURANTS = 5
    }
}
