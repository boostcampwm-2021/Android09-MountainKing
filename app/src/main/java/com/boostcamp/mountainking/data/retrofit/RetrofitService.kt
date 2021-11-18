package com.boostcamp.mountainking.data.retrofit

import com.boostcamp.mountainking.entity.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appId: String
    ): WeatherResponse
}