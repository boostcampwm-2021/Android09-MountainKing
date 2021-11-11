package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boostcamp.mountainking.data.LatLngAlt

@Entity
data class Tracking(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mountainName: String,
    val date: String?,
    val coordinates: List<LatLngAlt>,
    val duration: String,
    val distance: String?
)