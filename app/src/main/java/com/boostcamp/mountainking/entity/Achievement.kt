package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Achievement(
    @PrimaryKey
    var id: String,
    var name: String,
    var description: String,
    var thumbnailUrl: String,
    var type: AchievementType,
    var curProgress: Int,
    var maxProgress: Int,
    var isComplete: Boolean,
    var completeDate: Date,
    var score: Int
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
    val completeDateString: String get() = dateFormat.format(completeDate)
}