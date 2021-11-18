package com.boostcamp.mountainking.data

import androidx.lifecycle.MutableLiveData
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.entity.WeatherResponse

interface RepositoryInterface {
    suspend fun getMountain(id: Int): Mountain
    suspend fun getTracking(): List<Tracking>?
    suspend fun getAchievement(): List<Achievement>
    suspend fun getStatistics(): Statistics
    suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherResponse>
    suspend fun searchMountainName(name: String): List<Mountain>
    suspend fun searchMountainNameInCity(state: String, cityName: String, name: String): List<Mountain>

    suspend fun putTracking(tracking: Tracking)
    suspend fun updateStatistics()
    suspend fun updateAchievement(achievement: Achievement)
    suspend fun deleteTracking(tracking: Tracking)

    suspend fun updateStatistics(statistics: Statistics)

    var isRunning: Boolean
    var trackingMountain: String?
    var trackingMountainID: Int
    var curTime: MutableLiveData<String>
    var intTime: Int
    var curDistance: MutableLiveData<Int>
    var curStep: MutableLiveData<Int>
    var date: MutableLiveData<String>
    var locations: MutableList<LatLngAlt>
    var locationLiveData: MutableLiveData<List<LatLngAlt>>
}