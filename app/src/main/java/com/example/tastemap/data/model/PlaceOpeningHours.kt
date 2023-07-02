package com.example.tastemap.data.model

import com.squareup.moshi.Json

data class PlaceOpeningHours(
    @Json(name = "open_now") val openNow: Boolean?,
    @Json(name = "weekday_text") val weekdayText: List<String>?
)
