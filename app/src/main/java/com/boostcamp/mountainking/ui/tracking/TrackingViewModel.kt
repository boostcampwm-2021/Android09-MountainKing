package com.boostcamp.mountainking.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.util.Event
import com.boostcamp.mountainking.util.StringGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationServiceManager: LocationServiceManager,
    private val stringGetter: StringGetter
) : ViewModel() {

    private val _trackingTime = MutableLiveData<String>()
    val trackingTime: LiveData<String> get() = _trackingTime

    private val _trackingDistance = MutableLiveData<String>()
    val trackingDistance: LiveData<String> get() = _trackingDistance

    private val _checkPermission = MutableLiveData<Event<Unit>>()
    val checkPermission: LiveData<Event<Unit>> get() = _checkPermission

    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String> get() = _buttonText

    init {
        _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
    }

    fun toggleService() {
        if (locationServiceManager.isServiceRunning() == true) {
            _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
            locationServiceManager.stopService()
        } else {
            _checkPermission.value = Event(Unit)
        }
    }

    fun startService() {
        _buttonText.value = stringGetter.getString(R.string.title_stop_tracking)
        locationServiceManager.startService()
    }

    fun bindService() {
        locationServiceManager.bindService()
    }

    fun unbindService() {
        locationServiceManager.unBindService()
    }
}