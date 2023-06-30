package com.example.tastemap.data.model

import com.squareup.moshi.Json

data class PlaceDetailResult(
    val name: String,
    val rating: Double,
    @Json(name = "user_ratings_total") val userRatingTotal: Int,
    @Json(name = "current_opening_hours") val currentOpeningHours: PlaceOpeningHours?,
    @Json(name = "price_level") val priceLevel: Int?,
    val website: String?
)
