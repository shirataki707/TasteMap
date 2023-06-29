package com.example.tastemap.data.api.places

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    @GET("/maps/api/place/findplacefromtext/json")
    suspend fun fetchPlaceId(
        @Query("input") input: String,
        @Query("inputtype") inputType: String,
        @Query("key") key: String
    ): Response<PlacesApiIdResponse>

    @GET("/maps/api/place/details/json")
    suspend fun fetchDetail(
        @Query("place_id") placeId: String,
        @Query("key") key: String
    ): Response<PlacesApiDetailResponse>
}
