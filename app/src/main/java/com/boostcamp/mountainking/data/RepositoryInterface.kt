package com.boostcamp.mountainking.data

import com.boostcamp.mountainking.entity.Achievement

interface RepositoryInterface {
    suspend fun getMountain()
    suspend fun getTracking()
    suspend fun getAchievement(): List<Achievement>
    suspend fun getStatistics()
    suspend fun getWeather()

    suspend fun putTracking()
    suspend fun updateStatistics()
    suspend fun updateAchievement(achievement: Achievement)

    var isRunning: Boolean
}