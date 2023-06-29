package com.example.tastemap.data.api.places

import retrofit2.Response
import javax.inject.Inject

class PlacesApiClientImpl @Inject constructor(
    private val apiService: PlacesApiService
) : PlacesApiClient {
    override suspend fun fetchPlaceId(request: PlacesApiIdRequest): Response<PlacesApiIdResponse> {
        return apiService.fetchPlaceId(
            input = request.input,
            inputType = request.inputType,
            key = request.key
        )
    }

    override suspend fun fetchDetail(request: PlacesApiDetailRequest): Response<PlacesApiDetailResponse> {
        return apiService.fetchDetail(
            placeId = request.placeId,
            key = request.key
        )
    }
}
