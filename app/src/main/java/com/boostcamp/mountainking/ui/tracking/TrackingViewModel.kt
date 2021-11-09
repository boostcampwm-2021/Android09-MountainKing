package com.boostcamp.mountainking.ui.tracking

import android.util.Log
import androidx.lifecycle.*
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.util.Event
import com.boostcamp.mountainking.util.StringGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import com.boostcamp.mountainking.entity.Tracking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationServiceManager: LocationServiceManager,
    private val stringGetter: StringGetter,
    private val repository: RepositoryInterface
) : ViewModel() {


    val trackingTime: LiveData<String> get() = repository.curTime
    val trackingDistance: LiveData<Int> get() = repository.curDistance
    val date: LiveData<String> get() = repository.date

    private val _checkPermission = MutableLiveData<Event<Unit>>()
    val checkPermission: LiveData<Event<Unit>> get() = _checkPermission

    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String> get() = _buttonText

    private val _showDialog = MutableLiveData<Event<Unit>>()
    val showDialog: LiveData<Event<Unit>> get() = _showDialog

    init {
        if (repository.isRunning) {
            _buttonText.value = stringGetter.getString(R.string.title_stop_tracking)
        } else {
            _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
        }
    }

    fun toggleService() {
        if (repository.isRunning) {
            _buttonText.value = stringGetter.getString(R.string.title_start_tracking)
            locationServiceManager.stopService()

            CoroutineScope(Dispatchers.IO).launch { // TODO mountainName 추가 필요
                withContext(Dispatchers.Main) {
                    repository.putTracking(
                        Tracking(
                            id = 0,
                            repository.trackingMountain.toString(),
                            date.value,
                            "",
                            trackingTime.value.toString(),
                            trackingDistance.value.toString()
                        )
                    )
                    repository.trackingMountain = null
                }
            }
        } else {
            _showDialog.value = Event(Unit)
        }
    }

    fun checkPermission() {
        if (repository.trackingMountain != null) {
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
        if (repository.isRunning) {
            locationServiceManager.unBindService()
        }
    }
}