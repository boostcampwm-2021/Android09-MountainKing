package com.boostcamp.mountainking.data

import android.content.Context
import android.util.Log
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Mountain
import androidx.lifecycle.MutableLiveData
import com.boostcamp.mountainking.OPEN_WEATHER_KEY
import com.boostcamp.mountainking.data.retrofit.RetrofitApi
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.entity.WeatherResponse
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) : RepositoryInterface {
    // database
    private val appDatabase = AppDatabase.getInstance(context)
    private val mountainDao = MountainDatabase.getInstance(context).mountainDao()
    private val statisticsDao = appDatabase.statisticsDao()
    private val trackingDao = appDatabase.trackingDao()
    private val achievementDao = appDatabase.achievementDao()

    // variable
    override var isRunning = false
    override var trackingMountain: String? = null
    override var trackingMountainID = -1
    private val weatherApi = RetrofitApi.openWeatherServer
    override var curTime = MutableLiveData<String>()
    override var curDistance = MutableLiveData<Int>()
    override var curStep = MutableLiveData<Int>()
    override var intTime = 0
    override var date = MutableLiveData<String>()
    override var locations = mutableListOf<LatLngAlt>()
    override var locationLiveData = MutableLiveData<List<LatLngAlt>>()

    init {
        curStep.value = -1
    }

    override suspend fun getMountain(id: Int): Mountain = withContext(Dispatchers.IO) {
        mountainDao.getMountain(id)
    }

    override suspend fun getTracking(): List<Tracking> = withContext(Dispatchers.IO) {
        trackingDao.getTrackingData()
    }

    override suspend fun getAchievement(): List<Achievement> = withContext(Dispatchers.IO) {
        val namedMountainList = mountainDao.searchNamedMountain()
        val newAchievements = getInitAchievementList(namedMountainList)
        if (achievementDao.countData() == 0) {
            newAchievements.forEach {
                achievementDao.insert(it)
            }
        } else {
            achievementDao.getAchievementData().forEach { originalAchievement ->
                if (originalAchievement.thumbnailUrl.isEmpty()) {
                    newAchievements.find { it.id == originalAchievement.id }?.let {
                        achievementDao.updateThumbnailUrl(
                            it.thumbnailUrl,
                            originalAchievement.id
                        )
                    }
                }
            }
        }
        achievementDao.getAchievementData()
    }


    override suspend fun getStatistics(): Statistics = withContext(Dispatchers.IO) {
        statisticsDao.insert(Statistics())
        statisticsDao.getStatistics()
    }

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherResponse> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                weatherApi.getWeather(latitude, longitude, EXCLUDE_STRING, OPEN_WEATHER_KEY)
            }
        }

    override suspend fun searchMountainName(name: String, location: LatLng?): List<Mountain> {
        return withContext(Dispatchers.IO) {
            val mountainList = mountainDao.searchMountainName(name)
            if (location != null) {
                mountainList.sortedBy { location.distanceTo(LatLng(it.latitude, it.longitude)) }
            } else {
                mountainList
            }
        }
    }

    override suspend fun searchMountainNameInCity(
        state: String,
        cityName: String,
        name: String
    ): List<Mountain> {
        return withContext(Dispatchers.IO) {
            mountainDao.searchMountainNameInCity(state, cityName, name)
        }
    }

    override suspend fun putTracking(tracking: Tracking) {
        trackingDao.insert(tracking)
    }

    override suspend fun deleteTracking(tracking: Tracking) {
        trackingDao.delete(tracking)
    }

    override fun resetVariables() {
        trackingMountain = null
        locations.clear()
        locationLiveData.value = locations
        curTime.value = ""
        curDistance.value = -1
        curStep.value = -1
    }

    override suspend fun updateStatistics(): Unit = withContext(Dispatchers.IO) {
        statisticsDao.insert(Statistics())
        val statistics = statisticsDao.getStatistics()
        val mountainMap = statistics.mountainMap.toMutableMap()
        val trackingCountMap = statistics.trackingCountMap.toMutableMap()
        val dateString = date.value

        when (val count = mountainMap[trackingMountainID]) {
            null -> mountainMap[trackingMountainID] = 1
            else -> mountainMap[trackingMountainID] = count + 1
        }
        dateString?.let { it ->
            when (val count = trackingCountMap[it]) {
                null -> trackingCountMap[it] = 1
                else -> trackingCountMap[it] = count + 1
            }
        }
        curDistance.value?.let { statisticsDao.update(it, intTime, mountainMap, trackingCountMap) }
    }

    override suspend fun updateStatistics(statistics: Statistics) = withContext(Dispatchers.IO) {
        statisticsDao.update(statistics)
    }

    override suspend fun updateAchievement(achievement: Achievement) {
        achievementDao.updateAchievement(achievement)
    }

    companion object {
        private var instance: Repository? = null
        private const val EXCLUDE_STRING = "current,minutely,hourly,alerts"

        @JvmStatic
        fun getInstance(context: Context): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(context).also {
                    instance = it
                }
            }
    }
}