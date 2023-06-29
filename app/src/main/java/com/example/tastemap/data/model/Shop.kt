package com.example.tastemap.data.model

import android.text.style.URLSpan
import com.squareup.moshi.Json

data class Shop(
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val genre: Genre,
    val capacity: String,
    val urls: Urls,
    val open: String,
    val close: String,
    @Json(name = "non_smoking") val nonSmoking: String
)
