package com.example.tastemap.di

import com.example.tastemap.data.api.hotpepper.HotPepperApiClient
import com.example.tastemap.data.api.hotpepper.HotPepperApiClientImpl
import com.example.tastemap.data.api.hotpepper.HotPepperApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HotPepperApiModule {

    @Provides
    @Singleton
    @Named("HotPepperApi")
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        val BASE_URL = "http://webservice.recruit.co.jp"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("HotPepperApi") retrofit: Retrofit): HotPepperApiService {
        return retrofit.create(HotPepperApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiClient(
        apiService: HotPepperApiService
    ): HotPepperApiClient {
        return HotPepperApiClientImpl(apiService)
//        return HotPepperMockApiClientImpl()
    }
}
