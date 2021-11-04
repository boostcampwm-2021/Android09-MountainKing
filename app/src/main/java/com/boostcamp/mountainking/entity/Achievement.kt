package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Achievement(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var thumbnailUrl: String = "",
    var type: AchievementType = AchievementType.TRACKING_COUNT,
    var curProgress: Int = 0,
    var maxProgress: Int = 1,
    var isComplete: Boolean = false,
    var completeDate: Date? = Date(),
    var score: Int = 0
) {

    val completeDateString: String get() = completeDate?.let { SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR")).format(it) } ?: ""
}