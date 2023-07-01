package com.example.tastemap.data.model

data class Restaurant(
    val name: String,
    val rating: Double?,
    val userReviews: Int?,
    val location: Location?,
    val isOpenNow: Boolean?,
    val weekdayText: List<String>?,
    val priceLevel: Int?,
    val website: String?,
    val image: String?,
)
