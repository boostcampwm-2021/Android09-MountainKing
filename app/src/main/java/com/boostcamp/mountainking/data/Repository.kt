package com.boostcamp.mountainking.data

import android.content.Context
import android.util.Log
import com.boostcamp.mountainking.entity.Achievement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) : RepositoryInterface {

    private val mountainDao = MountainDatabase.getInstance(context).mountainDao()
    val database = AppDatabase.getInstance(context)?.achievementDao()
    override var isRunning = false

    override suspend fun getMountain() {
        //TODO("산정보 불러오기")
    }

    override suspend fun getTracking() {
        //TODO("등산기록불러오기")
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

    override suspend fun searchMountainName(name: String): List<String> {
        return withContext(Dispatchers.IO) {
            mountainDao.searchMountainName(name)
        }
    }

    override suspend fun putTracking() {
        //TODO("등산기록저장하기")
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