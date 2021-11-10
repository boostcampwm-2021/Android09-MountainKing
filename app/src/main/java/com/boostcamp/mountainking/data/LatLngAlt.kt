package com.boostcamp.mountainking.data

import android.location.Location

data class LatLngAlt(val latitude: Double, val longitude: Double, val altitude: Double) {
    companion object {
        fun fromLocation(location: Location): LatLngAlt {
            return LatLngAlt(location.latitude, location.longitude, location.altitude)
        }
    }
}