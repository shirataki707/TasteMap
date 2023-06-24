package com.example.tastemap.data.api.hotpepper

/*
 [参照] https://webservice.recruit.co.jp/doc/hotpepper/reference.html
 rangeは5段階．1. 300m, 2. 500m, 3. 1000m(default), 4. 2000m, 5. 3000m
 orderはソート順．1. 店名，2. ジャンルコード，3. 小エリアコード，4. おすすめ順(default)
 */

data class HotPepperApiRequest(
    val key: String = "e19e6c03dc5d49be",
    val lat: Double,
    val lng: Double,
    val range: Int,
    val keyword: String,
    val order: Int,
    val count: Int,
    val format: String = "json"
)