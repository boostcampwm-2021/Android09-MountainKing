package com.boostcamp.mountainking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Statistics(
    @PrimaryKey
    val id: Int = 0,
    val distance: Int,
    val time: Int,
    val mountainMap: Map<Int, Int>
)