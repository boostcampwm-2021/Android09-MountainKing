package com.boostcamp.mountainking.data

import android.content.Context
import android.util.Log
import com.boostcamp.mountainking.entity.Achievement

class Repository(private val context: Context) : RepositoryInterface {

    val database = AchievementDatabase.provideAchievementDao(context)

    override suspend fun getMountain() {
        //TODO("산정보불러오기")
    }

    override suspend fun getTracking() {
        //TODO("등산기록불러오기")
    }

    override fun getAchievement(): List<Achievement> {

        //Log.d("count", database.countData().toString())

        getInitAchievementList().forEach {
            //database.insert(it)
            Log.d("achievement", it.name)
        }
//                val list = database.getAchievementData()
//                list.forEach {
//                    Log.d("db", it.name)
//                }
//                list
        return getInitAchievementList()
    }

    override suspend fun getStatistics() {
        //TODO("통계불러오기")
    }

    override suspend fun getWeather() {
        //TODO("날씨불러오기")
    }

    override suspend fun putTracking() {
        //TODO("등산기록저장하기")
    }

    override suspend fun updateStatistics() {
        //TODO("통계최신화하기")
    }

    override suspend fun updateAchievement() {
        //TODO("업적최신화하기")
    }
}