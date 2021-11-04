package com.boostcamp.mountainking.data

import android.content.Context
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Repository(private val context: Context) : RepositoryInterface {
    override suspend fun getMountain() {
        //TODO("산정보불러오기")
    }

    override suspend fun getTracking() {
        //TODO("등산기록불러오기")
    }

    override suspend fun getAchievement(): Result<List<Achievement>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                getInitAchievementList()
            }
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