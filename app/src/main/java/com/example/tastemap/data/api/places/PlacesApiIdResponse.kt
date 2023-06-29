package com.example.tastemap.data.api.places

import com.example.tastemap.data.model.PlaceId

data class PlacesApiIdResponse(val candidates: List<PlaceId>, val status: String)
