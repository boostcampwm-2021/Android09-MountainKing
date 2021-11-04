package com.boostcamp.mountainking.util


import android.content.Context
import androidx.preference.PreferenceManager

const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"

fun requestingLocationUpdates(context: Context?): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context)
        .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
}

fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putBoolean(
            KEY_REQUESTING_LOCATION_UPDATES,
            requestingLocationUpdates
        )
        .apply()
}

