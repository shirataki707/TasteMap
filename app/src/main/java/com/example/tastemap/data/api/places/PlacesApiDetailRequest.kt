package com.example.tastemap.data.api.places

import com.example.tastemap.BuildConfig

data class PlacesApiDetailRequest(
    val placeId: String,
    val key: String = BuildConfig.GOOGLE_PLACES_API_KEY
)
