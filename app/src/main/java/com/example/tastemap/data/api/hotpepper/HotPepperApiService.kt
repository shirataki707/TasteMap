package com.example.tastemap.data.api.hotpepper

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HotPepperApiService {
    @GET("/hotpepper/gourmet/v1")
    suspend fun fetchShops(
        @Query("key") key: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("range") range: Int,
        @Query("genre") genre: String,
        @Query("keyword") keyword: String,
        @Query("order") order: Int,
        @Query("count") count: Int,
        @Query("format") format: String
    ): Response<HotPepperApiResponse>
}