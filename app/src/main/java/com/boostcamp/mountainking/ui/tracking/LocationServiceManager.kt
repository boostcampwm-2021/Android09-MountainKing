package com.boostcamp.mountainking.ui.tracking

import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LocationServiceManager(private val context: Context) {
    fun startService() {
        val intent = Intent(context, LocationService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}