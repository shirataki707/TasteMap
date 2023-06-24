package com.example.tastemap.data.repository

import com.example.tastemap.data.api.hotpepper.HotPepperApiClient
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.hotpepper.HotPepperApiResponse
import javax.inject.Inject

class HotPepperApiRepository @Inject constructor(
    private val apiClient: HotPepperApiClient
) {

    suspend fun fetchShops(request: HotPepperApiRequest): Result<HotPepperApiResponse> {

        return try {
            val response = apiClient.fetchShops(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Throwable("Request failed: ${response.errorBody()?.string()}"))
            }
        } catch (exception: Exception) {
            // 通信エラーやパースエラー
            Result.failure(exception)
        }
    }
}