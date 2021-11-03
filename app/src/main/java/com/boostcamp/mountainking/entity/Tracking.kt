package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tracking(
    @PrimaryKey
    val id: String,
    val mountainName: String,
    val startTime: String,
    val endTime: String,
    val coordinates: List<Pair<Float, Float>>,
    val length: Int
)