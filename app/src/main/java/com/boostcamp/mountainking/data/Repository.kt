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
    private val trackingDao = appDatabase?.trackingDao()
    private val achievementDao = appDatabase?.achievementDao()
    override var isRunning = false
    override var trackingMountain: String? = null
    override var curTime = MutableLiveData<String>()
    override var curDistance = MutableLiveData<Int>()
    override var date = MutableLiveData<String>()
    override var locations: List<LatLngAlt> = emptyList()

    override suspend fun getMountain() {
        //TODO("산정보 불러오기")
    }

    override suspend fun getTracking(): List<Tracking> = withContext(Dispatchers.IO) {
        trackingDao?.getTrackingData() ?: listOf()
    }

    override suspend fun getAchievement(): List<Achievement> = withContext(Dispatchers.IO) {
        if (achievementDao?.countData() == 0) {
            getInitAchievementList().forEach {
                achievementDao.insert(it)
            }
        }
        achievementDao?.getAchievementData() ?: listOf()
    }


    override suspend fun getStatistics() {
        //TODO("통계불러오기")
    }

    override suspend fun getWeather() {
        //TODO("날씨불러오기")
    }

    override suspend fun searchMountainName(name: String): List<Mountain> {
        return withContext(Dispatchers.IO) {
            mountainDao.searchMountainName(name)
        }
    }

    override suspend fun putTracking(tracking: Tracking) {
        trackingDao?.insert(tracking)
    }

    override suspend fun deleteTracking(tracking: Tracking) {
        trackingDao?.delete(tracking)
    }

    override suspend fun updateStatistics() {
        //TODO("통계최신화하기")
    }

    override suspend fun updateAchievement(achievement: Achievement) {
        achievementDao?.updateAchievement(achievement)
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