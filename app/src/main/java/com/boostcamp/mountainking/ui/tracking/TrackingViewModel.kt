package com.boostcamp.mountainking.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrackingViewModel : ViewModel() {

    private val _trackingTime = MutableLiveData<String>()
    val trackingTime: LiveData<String> get() = _trackingTime

    private val _trackingDistance = MutableLiveData<String>()
    val trackingDistance: LiveData<String> get() = _trackingDistance

}