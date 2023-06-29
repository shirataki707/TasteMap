package com.example.tastemap.data.api.places

import com.example.tastemap.data.model.PlaceDetailResult
import com.squareup.moshi.Json

data class PlacesApiDetailResponse(
    @Json(name = "html_attributions") val htmlAttributions: List<String>,
    val result: PlaceDetailResult,
    val status: String
)
