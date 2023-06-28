package com.example.tastemap.data.api.hotpepper

import com.example.tastemap.BuildConfig


/*
 [参照] https://webservice.recruit.co.jp/doc/hotpepper/reference.html
 rangeは5段階．1. 300m, 2. 500m, 3. 1000m(default), 4. 2000m, 5. 3000m
 orderはソート順．1. 店名，2. ジャンルコード，3. 小エリアコード，4. おすすめ順(default)
 */

data class HotPepperApiRequest(
    val key: String = BuildConfig.HOT_PEPPER_API_KEY,
    val lat: Double,
    val lng: Double,
    val range: Int,
    val genre: String,
    val keyword: String,
    val order: Int = 4,     // あとでシャッフルするからあんまり意味はないけど，100件以上結果がある時に有効
    val count: Int = 100,   // 100件を上限に取得して後でフィルタをかける
    val format: String = "json"
)