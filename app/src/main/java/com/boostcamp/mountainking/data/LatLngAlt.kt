package com.boostcamp.mountainking.data

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LatLngAlt(val latitude: Double, val longitude: Double, val altitude: Double)
    : Parcelable {
    companion object {
        fun fromLocation(location: Location): LatLngAlt {
            return LatLngAlt(location.latitude, location.longitude, location.altitude)
        }
    }
}