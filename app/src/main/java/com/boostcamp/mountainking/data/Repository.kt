package com.boostcamp.mountainking.data

import android.content.Context
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Mountain
import androidx.lifecycle.MutableLiveData
import com.boostcamp.mountainking.entity.Tracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) : RepositoryInterface {

    private val appDatabase = AppDatabase.getInstance(context)

    private val mountainDao = MountainDatabase.getInstance(context).mountainDao()
    private val statisticsDao = appDatabase.statisticsDao()
    override var isRunning = false
    override var trackingMountain: String? = null
    override var trackingMountainID = -1
    private val trackingDao = appDatabase.trackingDao()
    private val achievementDao = appDatabase.achievementDao()
    override var curTime = MutableLiveData<String>()
    override var intTime = 0
    override var curDistance = MutableLiveData<Int>()
    override var date = MutableLiveData<String>()
    override var locations: List<LatLngAlt> = emptyList()

    override suspend fun getMountain() {
        //TODO("산정보 불러오기")
    }

    override suspend fun getTracking(): List<Tracking> = withContext(Dispatchers.IO) {
        trackingDao.getTrackingData()
    }

    override suspend fun getAchievement(): List<Achievement> = withContext(Dispatchers.IO) {
        if (achievementDao.countData() == 0) {
            val namedMountainList = mountainDao.searchNamedMountain()
            getInitAchievementList(namedMountainList).forEach {
                achievementDao.insert(it)
            }
        }
        achievementDao.getAchievementData()
    }


    override suspend fun getStatistics() : Statistics  = withContext(Dispatchers.IO){
        statisticsDao.insert(Statistics())
        statisticsDao.getStatistics()
    }

    override suspend fun getWeather() {
        //TODO("날씨불러오기")
    }

    override suspend fun searchMountainName(name: String): List<Mountain> {
        return withContext(Dispatchers.IO) {
            mountainDao.searchMountainName(name)
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

        @JvmStatic
        fun getInstance(context: Context): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(context).also {
                    instance = it
                }
            }
    }
}