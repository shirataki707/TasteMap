package com.example.tastemap.data.repository

import com.example.tastemap.data.api.places.PlacesApiClient
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiDetailResponse
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.api.places.PlacesApiIdResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesApiRepository @Inject constructor(
    private val apiClient: PlacesApiClient,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchPlaceId(request: PlacesApiIdRequest): Result<PlacesApiIdResponse> {

        return withContext(defaultDispatcher) {
            try {
                val response = apiClient.fetchPlaceId(request)

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

    suspend fun fetchPlaceDetail(request: PlacesApiDetailRequest): Result<PlacesApiDetailResponse> {

        return withContext(defaultDispatcher) {
            try {
                val response = apiClient.fetchDetail(request)

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
