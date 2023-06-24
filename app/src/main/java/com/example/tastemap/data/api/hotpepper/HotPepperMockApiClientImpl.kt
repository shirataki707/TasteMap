package com.example.tastemap.data.api.hotpepper

import com.example.tastemap.data.model.Genre
import com.example.tastemap.data.model.HotPepperApiResults
import com.example.tastemap.data.model.Shop
import com.example.tastemap.data.model.Urls
import retrofit2.Response
import javax.inject.Inject

class HotPepperMockApiClientImpl @Inject constructor(): HotPepperApiClient {
    override suspend fun fetchShops(request: HotPepperApiRequest): Response<HotPepperApiResponse> {
        val dummyResponse = createDummyResponse()
        return Response.success(dummyResponse)
    }

    private fun createDummyResponse(): HotPepperApiResponse {
        val shopList = listOf(
            Shop(
                name = "居酒家 ぐらんま",
                address = "福岡県飯塚市堀池１５－７",
                lat = 33.6281194191,
                lng = 130.6860219343,
                genre = Genre(
                    name = "居酒屋",
                    catch = "ボリューム◎大満足のコースが人気♪"
                ),
                capacity = 60,
                urls = Urls(
                    pc = "https://www.hotpepper.jp/strJ000569668/?vos=nhppalsa000016"
                ),
                open = "月～土、祝前日: 17:30～23:00 （料理L.O. 22:00 ドリンクL.O. 22:30）日、祝日: 17:30～22:00 （料理L.O. 21:00 ドリンクL.O. 21:30）",
                close = "不定休",
                nonSmoking = "全面禁煙"
            ),
            Shop(
                name = "焼肉 ウエスト 飯塚店",
                address = "福岡県飯塚市枝国216-5",
                lat = 33.6307700036,
                lng = 130.6743326421,
                genre = Genre(
                    name = "焼肉・ホルモン",
                    catch = "12/31～1/3ランチメニュー行っておりません"
                ),
                capacity = 138,
                urls = Urls(
                    pc = "https://www.hotpepper.jp/strJ000964470/?vos=nhppalsa000016"
                ),
                open = "月～金、祝前日: 11:30～15:00 （料理L.O. 14:30 ドリンクL.O. 14:30）17:00～22:30 （料理L.O. 22:00 ドリンクL.O. 22:00）土、日、祝日: 11:30～22:30 （料理L.O. 22:00 ドリンクL.O. 22:00）",
                close = "2020年9月1日より＜年中無休＞",
                nonSmoking = "全面禁煙"
            )
        )

        return HotPepperApiResponse(
            results = HotPepperApiResults(
                resultsAvailable = 97,
                resultsReturned = 10,
                shop = shopList
            )
        )
    }

}