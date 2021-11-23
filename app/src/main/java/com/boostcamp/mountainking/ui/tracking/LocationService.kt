package com.boostcamp.mountainking.ui.tracking

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.boostcamp.mountainking.MainActivity
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.LatLngAlt
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.util.setRequestingLocationUpdates
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import java.time.LocalDate

class LocationService : LifecycleService(), SensorEventListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var serviceHandler: Handler
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var sensorManager: SensorManager
    private lateinit var stepDetectorSensor: Sensor

    private val binder = LocationBinder()
    private var location: Location? = null

    private var curTime: Int = 0
    private var curDistance: Int = 0
    private var curStep: Int = 0

    private var tempStep: Int = 0

    private var isBound = true
    private val repository = Repository.getInstance(this)

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // 걸음수가 늘어난 경우에만 new location이라고 취급
                if (tempStep < curStep) {
                    onNewLocation(locationResult.lastLocation)
                    repository.locations.add(LatLngAlt.fromLocation(locationResult.lastLocation))
                    repository.locationLiveData.postValue(repository.locations)
                }
                tempStep = curStep
            }
        }
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createSensor()
        createLocationRequest()
        getLastLocation()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG, "onStartCommand")

        repository.isRunning = true
        repository.date.postValue(LocalDate.now().toString())
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("${repository.trackingMountain?.substringBefore("(")} 등산 중...")
            .setContentText("시간 : ${timeConverter(curTime)} 거리 : ${curDistance}m  걸음 수 : $curStep 걸음")
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_notification)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        lifecycleScope.launch(Dispatchers.IO) {
            while (isBound) {
                delay(1000)
                notificationBuilder.setContentText(
                    "시간 : ${timeConverter(++curTime)} 거리 : ${curDistance}m  걸음 수 : $curStep 걸음")
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                repository.curTime.postValue(timeConverter(curTime))
                repository.intTime = curTime
            }
        }
        requestLocationUpdates()
        return START_NOT_STICKY
    }

    private fun timeConverter(time: Int): String {
        val div = time / 60
        val hour = div / 60
        val minute = div - (hour * 60)
        val second = time - (div * 60)

        return String.format("%02d:%02d:%02d", hour, minute, second)
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
            smallestDisplacement = SMALLEST_DISPLACEMENT
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        setRequestingLocationUpdates(this, true)
        curDistance = 0
        curStep = 0
        tempStep = 0
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                serviceHandler.looper
            )
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, false)
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        repository.isRunning = false
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            setRequestingLocationUpdates(this, false)
            stopForeground(true)
            stopSelf()
        } catch (unlikely: SecurityException) {
            setRequestingLocationUpdates(this, true)
            Log.e(
                TAG, "Lost location permission. Could not remove updates. $unlikely"
            )
        }
    }

    // locationCallback 안에서 실행될 메소드
    private fun onNewLocation(lastLocation: Location) {
        val distance = location?.distanceTo(lastLocation)?.toInt()
        curDistance += distance ?: 0
        repository.curDistance.postValue(curDistance)
        this.location = lastLocation
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, lastLocation)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Log.i(TAG, "onBind")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceHandler.removeCallbacksAndMessages(null)
        sensorManager.unregisterListener(this)
    }

    inner class LocationBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    private fun createSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null ) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST)
            Log.i(TAG, "connected step detector sensor!")
        }
        else {
            Log.i(TAG, "this device hasn't step counter sensor!")
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.i(TAG, "onSensorChanged()")
        when(event.sensor.type) {
            Sensor.TYPE_STEP_DETECTOR -> {
                if(event.values[0] == 1.0f) {
                    repository.curStep.postValue( ++ curStep)
                    Log.i(TAG, "$curStep 걸음")
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // implements StepEventListener
    }

    companion object {
        private val TAG = LocationService::class.java.simpleName
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000.toLong()
        private const val SMALLEST_DISPLACEMENT = 0F
        private const val PACKAGE_NAME = "com.boostcamp.mountainking.ui.tracking.locationservice"
        private const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        private const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "primary_notification_channel"
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }
}