package com.example.tastemap.di

import com.example.tastemap.data.api.hotpepper.HotPepperApiClient
import com.example.tastemap.data.api.hotpepper.HotPepperApiClientImpl
import com.example.tastemap.data.api.hotpepper.HotPepperApiService
import com.example.tastemap.data.api.places.PlacesApiClient
import com.example.tastemap.data.api.places.PlacesApiClientImpl
import com.example.tastemap.data.api.places.PlacesApiService
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
object PlacesApiModule {

    @Provides
    @Singleton
    @Named("PlacesApi")
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        val BASE_URL = "https://maps.googleapis.com"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("PlacesApi") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiClient(
        apiService: PlacesApiService
    ): PlacesApiClient {
        return PlacesApiClientImpl(apiService)
//        return PlacesMockApiClientImpl()
    }
}