package com.example.tastemap.data.model

import com.squareup.moshi.Json

data class PlaceDetailResult(
    val rating: Double,
    @Json(name = "user_ratings_total") val userRatingTotal: Int
)
