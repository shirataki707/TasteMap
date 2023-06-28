package com.example.tastemap.data.model


/*
    reviewPriorities:
        0: 好みなし
        1: 星の数重視
        2: レビュー数重視
    smokingPriorities:
        0: 好みなし
        1: 禁煙席のみ
        2: 喫煙席のみ
 */
data class UserPreferences(
    val reviewPriorities: Int = 0,
    val smokingPriorities: Int = 0
)