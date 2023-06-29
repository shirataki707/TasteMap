package com.example.tastemap.data.api.places

import com.example.tastemap.BuildConfig

data class PlacesApiIdRequest(
    val input: String,
    val inputType: String = "textquery",
    val key: String = BuildConfig.GOOGLE_PLACES_API_KEY
)
