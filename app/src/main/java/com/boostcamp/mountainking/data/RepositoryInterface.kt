package com.boostcamp.mountainking.data

import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Mountain

interface RepositoryInterface {
    suspend fun getMountain()
    suspend fun getTracking()
    suspend fun getAchievement(): List<Achievement>
    suspend fun getStatistics()
    suspend fun getWeather()
    suspend fun searchMountainName(name: String): List<Mountain>

    suspend fun putTracking()
    suspend fun updateStatistics()
    suspend fun updateAchievement(achievement: Achievement)

    var isRunning: Boolean
    var trackingMountain: Mountain?
}