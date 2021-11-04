package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boostcamp.mountainking.data.Statistics
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Achievement(
    @PrimaryKey
    var id: Long,
    var name: String,
    var description: String,
    var thumbnailUrl: String,
    var type: AchievementType,
    var curProgress: Int,
    var maxProgress: Int,
    var isComplete: Boolean,
    var completeDate: Date?,
    var score: Int,
    // 업적 타입에 따라 필요한 데이터 전달
    var typeArgument: Any? = null,
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
    val completeDateString: String get() = completeDate?.let { dateFormat.format(it) } ?: ""

    fun progressAchievement(statistics: Statistics) {
        curProgress = when (type) {
            AchievementType.TRACKING_TOTAL_DISTANCE -> {
                statistics.distance
            }
            AchievementType.TRACKING_TOTAL_COUNT -> {
                statistics.mountainMap.values.sum()
            }
            AchievementType.TRACKING_PERIOD_COUNT -> TODO()
            AchievementType.MOUNTAIN_COUNT -> {
                statistics.mountainMap[typeArgument]?:0
            }
            AchievementType.MOUNTAIN_KIND_COUNT -> {
                statistics.mountainMap.filter { entry ->
                    entry.value != 0
                }.filter { entry ->
                    (typeArgument as List<Int>).contains(entry.key)
                }.size
            }
        }
    }
}