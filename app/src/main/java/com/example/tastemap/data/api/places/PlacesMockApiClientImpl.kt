package com.example.tastemap.data.api.places

import com.example.tastemap.data.model.PlaceDetailResult
import com.example.tastemap.data.model.PlaceId
import com.example.tastemap.data.model.PlaceOpeningHours
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

// [NOTE] PlaceAPIが有料だから開発中はMockにしよう
class PlacesMockApiClientImpl @Inject constructor() : PlacesApiClient {
    override suspend fun fetchPlaceId(request: PlacesApiIdRequest): Response<PlacesApiIdResponse> {
        delay(1000) // simulate network delay

        val placeIds = List(10) { PlaceId("place_id_$it") }
        return Response.success(PlacesApiIdResponse(placeIds, "OK"))
    }

    override suspend fun fetchDetail(request: PlacesApiDetailRequest): Response<PlacesApiDetailResponse> {
        delay(1000) // simulate network delay

        val placeDetailResults = List(10) { index ->
            PlaceDetailResult(
                name = "Place $index",
                rating = (0..5).random().toDouble(),
                userRatingTotal = (1..1000).random(),
                currentOpeningHours = PlaceOpeningHours(
                    openNow = listOf(true, false).random(),
                    weekdayText = List(7) { day -> "Day $day: Open from 9:00 to 18:00" }
                ),
                priceLevel = (1..4).random(),
                website = "https://www.place$index.com"
            )
        }

        return Response.success(PlacesApiDetailResponse(emptyList(), placeDetailResults.first(), "OK"))
    }
}
