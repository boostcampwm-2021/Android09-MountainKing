package com.boostcamp.mountainking.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitApi {
    private const val openWeatherBaseUrl = "https://api.openweathermap.org/"

    private val openWeatherRetrofit = Retrofit.Builder()
        .baseUrl(openWeatherBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient())
        .build()

    val openWeatherServer: RetrofitService = openWeatherRetrofit.create(RetrofitService::class.java)

    fun httpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        return OkHttpClient.Builder()
            .addInterceptor(logging).build()
    }
}