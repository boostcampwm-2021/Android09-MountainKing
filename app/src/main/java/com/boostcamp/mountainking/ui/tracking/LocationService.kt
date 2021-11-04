package com.boostcamp.mountainking.ui.tracking

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.boostcamp.mountainking.MainActivity
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.util.setRequestingLocationUpdates
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private val binder = LocationBinder()
    private var location: Location? = null
    private val _locationList = mutableListOf<Location>()
    val locationList: List<Location> get() = _locationList

    private var curTime: Int = 0
    private var curDistance: Int = 0
    lateinit var serviceHandler: Handler

    private var isBound = true

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
                _locationList.add(locationResult.lastLocation)
            }
        }
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createLocationRequest()
        getLastLocation()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("등산 이력 기록중 ...")
            .setContentText("시간 : $curTime")
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_achievement_svgrepo_com)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        CoroutineScope(Dispatchers.IO).launch {
            while(isBound) {
                delay(1000)
                notificationBuilder.setContentText("시간 : ${++curTime}")
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }

        return START_STICKY
    }

    // notification channel 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "MyApp notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "등산 위치 추적"

        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        location = task.result
                        Log.d(TAG, location.toString())
                    } else {
                        Log.e(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        setRequestingLocationUpdates(this, true)

        try {
            Looper.myLooper()?.let {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback, it
                )
            }
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, false)
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    // locationCallback 안에서 실행될 메소드
    private fun onNewLocation(lastLocation: Location) {
        val distance = location?.distanceTo(lastLocation)?.toInt()
        curDistance += distance ?: 0
        Log.i(TAG, "New location: $lastLocation distance: $curDistance")

        this.location = lastLocation
        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, lastLocation)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, "onBind")
        return binder
    }

    inner class LocationBinder : Binder() {
        fun getService() : LocationService = this@LocationService
    }

    companion object {
        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "primary_notification_channel"
        private val TAG = LocationService::class.java.simpleName
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000.toLong()
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
        private const val PACKAGE_NAME = "com.boostcamp.mountainking.ui.tracking.locationservice"
        private const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        private const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

    }
}