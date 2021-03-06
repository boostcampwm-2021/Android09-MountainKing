package com.boostcamp.mountainking.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Mountain(
    @PrimaryKey
    val id: Int,
    val mountainName: String?,
    val locationName: String?,
    val namedMountainDetails: String?,
    val mountainHeight: Int?,
    val mountainOrganization: String?,
    val mountainSummary: String?,
    val mountainDetails: String?,
    val transportationDetails: String?,
    val tourDetails: String?,
    val mountainImageURL: String,
    val mountainSubtitle: String?,
    val latitude: Double,
    val longitude: Double,
) {
    override fun toString(): String {
        return "$mountainName($locationName)"
    }
}
