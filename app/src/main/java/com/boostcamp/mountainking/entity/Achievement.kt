package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boostcamp.mountainking.data.Statistics
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    var mountainIdList: List<Int> = listOf(),
    var period: Int? = null,
) {

    val completeDateString: String
        get() = completeDate?.let {
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale("ko", "KR")
            ).format(it)
        } ?: ""

    fun progressAchievement(statistics: Statistics): Boolean {
        if (isComplete) return false
        curProgress = when (type) {
            AchievementType.TRACKING_TOTAL_DISTANCE -> {
                statistics.distance
            }
            AchievementType.TRACKING_TOTAL_COUNT -> {
                statistics.mountainMap.values.sum()
            }
            AchievementType.TRACKING_PERIOD_COUNT -> {
                var sum = 0
                var date = LocalDate.now()
                period?.let { day ->
                    repeat(day) {
                        val dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        sum += statistics.trackingCountMap[dateStr] ?: 0
                        date = date.minusDays(1)
                    }
                }
                sum
            }
            AchievementType.MOUNTAIN_COUNT -> {
                mountainIdList.fold(0) { total, i ->
                    total + (statistics.mountainMap[i] ?: 0)
                }
            }
            AchievementType.MOUNTAIN_KIND_COUNT -> {
                statistics.mountainMap.filter { entry ->
                    entry.value != 0
                }.size
            }
        }
        if (curProgress >= maxProgress) {
            completeAchievement()
        }
        return true
    }

    private fun completeAchievement() {
        isComplete = true
        completeDate = Date()
    }
}