package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tracking(
    @PrimaryKey
    var id: String,
    var date: Date,
    var mountainName: String,
    var startTime: String,
    var endTime: String,
    var coordinates: List<Pair<Float, Float>>,
    var lenght: Int
)