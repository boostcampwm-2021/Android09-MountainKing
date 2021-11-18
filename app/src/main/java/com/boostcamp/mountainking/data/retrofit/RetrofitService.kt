package com.boostcamp.mountainking.data.retrofit

import com.boostcamp.mountainking.entity.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
//?lat={lat}&lon={lon}&exclude={exclude}&appid={appid}
interface RetrofitService {
    @GET("data/2.5/onecall/timemachine")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appId: String
    ): WeatherResponse

    @GET("data/2.5/onecall?lat=33.44&lon=-94.04&exclude=current,minutely,hourly,alerts&appid=67a90b7a5f9282c3f042fd7cf1c16b0b")
    suspend fun getWeatherTest(): WeatherResponse
}