package com.boostcamp.mountainking.data

import android.content.Context
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Mountain
import androidx.lifecycle.MutableLiveData
import com.boostcamp.mountainking.entity.Tracking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Repository(context: Context) : RepositoryInterface {

    private val mountainDao = MountainDatabase.getInstance(context).mountainDao()
    val database = AppDatabase.getInstance(context)?.achievementDao()
    override var isRunning = false
    override var trackingMountain: String? = null
    val trackingDatabase = AppDatabase.getInstance(context)?.trackingDao()
    override var curTime = MutableLiveData<String>()
    override var curDistance = MutableLiveData<Int>()
    override var date = MutableLiveData<String>()
    var coordinates = MutableLiveData<List<Pair<Float, Float>>>()

    override suspend fun getMountain() {
        //TODO("산정보 불러오기")
    }

    override suspend fun getTracking(): List<Tracking> = withContext(Dispatchers.IO) {
        trackingDatabase?.getTrackingData() ?: listOf()
    }

    override suspend fun getAchievement(): List<Achievement> = withContext(Dispatchers.IO) {
        if (database?.countData() == 0) {
            getInitAchievementList().forEach {
                database.insert(it)
            }
        }
        database?.getAchievementData() ?: listOf()
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
        trackingDatabase?.insert(tracking)
    }

    override suspend fun updateStatistics() {
        //TODO("통계최신화하기")
    }

    override suspend fun updateAchievement(achievement: Achievement) {
        database?.updateAchievement(achievement)
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