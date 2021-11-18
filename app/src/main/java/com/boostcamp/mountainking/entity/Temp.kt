package com.boostcamp.mountainking.entity

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Temp(
    @SerializedName("day")
    val day: Double,
    @SerializedName("eve")
    val eve: Double,
    @SerializedName("max")
    val max: Double,
    @SerializedName("min")
    val min: Double,
    @SerializedName("morn")
    val morn: Double,
    @SerializedName("night")
    val night: Double
) {
    val maxString: String get() = (max - K).roundToInt().toString() + tempUnit
    val minString: String get() = (min - K).roundToInt().toString() + tempUnit

    companion object {
        const val K = 273
        const val tempUnit = "℃"
    }
}