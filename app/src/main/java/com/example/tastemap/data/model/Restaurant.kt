package com.example.tastemap.data.model

import android.media.Rating

data class Restaurant(
    val name: String,
    val rating: Double,
    val userReviews: Int,
    val location: Location
)
