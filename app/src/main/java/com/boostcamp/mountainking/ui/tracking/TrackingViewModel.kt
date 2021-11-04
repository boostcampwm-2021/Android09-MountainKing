package com.boostcamp.mountainking.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationServiceManager: LocationServiceManager
) : ViewModel() {

    private val _trackingTime = MutableLiveData<String>()
    val trackingTime: LiveData<String> get() = _trackingTime

    private val _trackingDistance = MutableLiveData<String>()
    val trackingDistance: LiveData<String> get() = _trackingDistance


    fun startService() {
        locationServiceManager.startService()
    }

    fun bindService() {
        locationServiceManager.bindService()
    }

    fun unbindService() {
        locationServiceManager.unBindService()
    }
}