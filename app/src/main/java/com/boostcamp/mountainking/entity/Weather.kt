package com.boostcamp.mountainking.entity

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
) {
    val iconUrl: String get() = "http://openweathermap.org/img/wn/$icon@2x.png"
}