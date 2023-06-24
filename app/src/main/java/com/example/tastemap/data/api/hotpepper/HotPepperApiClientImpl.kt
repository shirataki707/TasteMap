package com.example.tastemap.data.api.hotpepper

import retrofit2.Response
import javax.inject.Inject

class HotPepperApiClientImpl @Inject constructor(
    private val apiService: HotPepperApiService
) : HotPepperApiClient {

    override suspend fun fetchShops(request: HotPepperApiRequest): Response<HotPepperApiResponse> {
        return apiService.fetchShops(
            key = request.key,
            lat = request.lat,
            lng = request.lng,
            range = request.range,
            keyword = request.keyword,
            order = request.order,
            count = request.count,
            format = request.format
        )
    }
}