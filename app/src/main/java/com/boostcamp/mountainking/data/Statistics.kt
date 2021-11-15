package com.boostcamp.mountainking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Statistics(
    @PrimaryKey
    val id: Int = 0,
    var distance: Int = 0,
    var time: Int = 0,
    var mountainMap: Map<Int, Int> = mapOf(),
    var trackingCountMap: Map<String, Int> = mapOf(),
)