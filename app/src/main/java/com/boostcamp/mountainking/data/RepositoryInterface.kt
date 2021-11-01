package com.boostcamp.mountainking.data

interface RepositoryInterface {
    suspend fun getMountain()
    suspend fun getTracking()
    suspend fun getAchievement()
    suspend fun getStatistics()
    suspend fun getWeather()

    suspend fun putTracking()
    suspend fun updateStatistics()
    suspend fun updateAchievement()
}