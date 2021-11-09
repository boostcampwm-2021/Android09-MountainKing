package com.boostcamp.mountainking.data

import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Tracking

interface RepositoryInterface {
    suspend fun getMountain()
    suspend fun getTracking(): List<Tracking>?
    suspend fun getAchievement(): List<Achievement>
    suspend fun getStatistics()
    suspend fun getWeather()

    suspend fun putTracking(tracking: Tracking)
    suspend fun updateStatistics()
    suspend fun updateAchievement(achievement: Achievement)
}