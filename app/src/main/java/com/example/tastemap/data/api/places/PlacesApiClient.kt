package com.example.tastemap.data.api.places

import retrofit2.Response

interface PlacesApiClient {

    suspend fun fetchPlaceId(request: PlacesApiIdRequest): Response<PlacesApiIdResponse>

    suspend fun fetchDetail(request: PlacesApiDetailRequest): Response<PlacesApiDetailResponse>
}