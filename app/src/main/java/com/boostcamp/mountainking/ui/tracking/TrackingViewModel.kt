package com.boostcamp.mountainking.ui.tracking

import android.util.Log
import androidx.lifecycle.*
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.util.Event
import com.boostcamp.mountainking.util.StringGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationServiceManager: LocationServiceManager,
    private val stringGetter: StringGetter,
    private val repository: Repository
) : ViewModel() {

    val trackingTime: LiveData<Int> get() = repository.curTime
    val trackingDistance: LiveData<Int> get() = repository.curDistance

    private val _checkPermission = MutableLiveData<Event<Unit>>()
    val checkPermission: LiveData<Event<Unit>> get() = _checkPermission

    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String> get() = _buttonText

    init {
        if (repository.isRunning) {
            _buttonText.value = stringGetter.getString(R.string.title_stop_tracking)
        } else {
            _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
        }
    }

    var count = 9 // TODO delete
    fun toggleService() {
        if (repository.isRunning) {
            _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
            locationServiceManager.stopService()
            // 여기서 저장
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    repository.putTracking(Tracking(id = 0, "", "", "", "", trackingDistance.value.toString()))
                }
            }
        } else {
            _checkPermission.value = Event(Unit)
            CoroutineScope(Dispatchers.IO).launch { // TODO delete
                withContext(Dispatchers.Main) {
                    Log.d("temp", "${repository.getTracking().size}")
                }
            }
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
        if (repository.isRunning) {
            locationServiceManager.unBindService()
        }
    }
}