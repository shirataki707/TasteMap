package com.example.tastemap.data.repository

import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.hotpepper.HotPepperApiResponse
import com.example.tastemap.data.api.places.PlacesApiClient
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiDetailResponse
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.data.api.places.PlacesApiIdResponse
import javax.inject.Inject

class PlacesApiRepository @Inject constructor(
    private val apiClient: PlacesApiClient
) {

    suspend fun fetchPlaceId(request: PlacesApiIdRequest): Result<PlacesApiIdResponse> {

        return try {
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

    suspend fun fetchPlaceDetail(request: PlacesApiDetailRequest): Result<PlacesApiDetailResponse> {

        return try {
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