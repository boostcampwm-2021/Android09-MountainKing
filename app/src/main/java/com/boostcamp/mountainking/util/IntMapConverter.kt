package com.boostcamp.mountainking.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntMapConverter {

    companion object {
        @JvmStatic
        @TypeConverter
        fun fromString(value: String): Map<Int, Int> {
            val mapType = object : TypeToken<Map<Int, Int>>() {}.type
            val gson = Gson()
            return gson.fromJson(value, mapType)
        }

        @JvmStatic
        @TypeConverter
        fun toString(map: Map<Int, Int>): String {
            val gson = Gson()
            return gson.toJson(map)
        }
    }
}