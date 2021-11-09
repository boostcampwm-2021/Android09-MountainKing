package com.boostcamp.mountainking.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*

class Repository(context: Context) : RepositoryInterface {

    val mountainDao = MountainDatabase.getInstance(context).mountainDao()
    val database = AppDatabase.getInstance(context)?.achievementDao()
    val trackingDatabase = AppDatabase.getInstance(context)?.trackingDao()
    var isRunning = false
    var curTime = MutableLiveData<String>()
    var curDistance = MutableLiveData<Int>()
    var date = MutableLiveData<String>()
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