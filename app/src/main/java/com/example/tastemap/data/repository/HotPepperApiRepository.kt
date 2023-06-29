package com.example.tastemap.data.repository

import com.example.tastemap.data.api.hotpepper.HotPepperApiClient
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.hotpepper.HotPepperApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HotPepperApiRepository @Inject constructor(
    private val apiClient: HotPepperApiClient,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun fetchShops(request: HotPepperApiRequest): Result<HotPepperApiResponse> {
        return withContext(defaultDispatcher) {
            try {
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
}
