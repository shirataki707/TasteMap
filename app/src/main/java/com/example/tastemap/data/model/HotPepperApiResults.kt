package com.example.tastemap.data.model

import com.squareup.moshi.Json

data class HotPepperApiResults(
    @Json(name = "results_available") val resultsAvailable: Int,
    @Json(name = "results_returned") val resultsReturned: Int,
    val shop: List<Shop>
)
