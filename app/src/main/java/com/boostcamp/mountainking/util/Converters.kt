package com.boostcamp.mountainking.util

import androidx.room.TypeConverter
import com.boostcamp.mountainking.data.LatLngAlt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {

    @TypeConverter
    fun stringToMap(value: String): Map<Int, Int> {
        val mapType = object : TypeToken<Map<Int, Int>>() {}.type
        val gson = Gson()
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun mapToString(map: Map<Int, Int>): String {
        val gson = Gson()
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun latLngAltListToString(latLngAltList: List<LatLngAlt>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<LatLngAlt>>() {}.type
        return gson.toJson(latLngAltList, type)
    }

    @TypeConverter
    fun stringToLatLngAltList(latLatAltString: String): List<LatLngAlt> {
        val gson = Gson()
        val type =
            object : TypeToken<List<LatLngAlt?>?>() {}.type
        return gson.fromJson(latLatAltString, type)
    }
}