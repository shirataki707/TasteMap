package com.example.tastemap.data.api.hotpepper

import retrofit2.Response

interface HotPepperApiClient {
    suspend fun fetchShops(request: HotPepperApiRequest): Response<HotPepperApiResponse>
}
