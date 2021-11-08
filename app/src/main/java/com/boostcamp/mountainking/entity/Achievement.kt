package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boostcamp.mountainking.data.Statistics
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Achievement(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var thumbnailUrl: String = "",
    var type: AchievementType = AchievementType.TRACKING_TOTAL_COUNT,
    var curProgress: Int = 0,
    var maxProgress: Int = 1,
    var isComplete: Boolean = false,
    var completeDate: Date? = Date(),
    var score: Int = 0,
) {

    val completeDateString: String
        get() = completeDate?.let {
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale("ko", "KR")
            ).format(it)
        } ?: ""

    fun progressAchievement(statistics: Statistics) {
        if(isComplete) return
        curProgress = when (type) {
            AchievementType.TRACKING_TOTAL_DISTANCE -> {
                statistics.distance
            }
            AchievementType.TRACKING_TOTAL_COUNT -> {
                statistics.mountainMap.values.sum()
            }
            AchievementType.TRACKING_PERIOD_COUNT -> { 0 }
            AchievementType.MOUNTAIN_COUNT -> {
                statistics.mountainMap[0] ?: 0
            }
            AchievementType.MOUNTAIN_KIND_COUNT -> {
                statistics.mountainMap.filter { entry ->
                    entry.value != 0
                }.size
            }
        }
        if(curProgress >= maxProgress) {
            completeAchievement()
        }
    }

    private fun completeAchievement() {
        isComplete = true
        completeDate = Date()
    }
}