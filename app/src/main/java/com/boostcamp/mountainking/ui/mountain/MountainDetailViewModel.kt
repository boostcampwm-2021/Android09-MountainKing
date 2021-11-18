package com.boostcamp.mountainking.ui.mountain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.entity.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainDetailViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainLiveData = MutableLiveData<Mountain?>()
    val mountainLiveData: LiveData<Mountain?> get() = _mountainLiveData
    private val _weatherLiveData = MutableLiveData<WeatherResponse>()
    val weatherLiveData: LiveData<WeatherResponse> get() = _weatherLiveData

    fun loadMountain(id: Int) = viewModelScope.launch {
        val curMountain = repository.getMountain(id)
        _mountainLiveData.value = curMountain
        repository.getWeather(curMountain.latitude, curMountain.longitude)
            .onSuccess {
                _weatherLiveData.value = it
                Log.d("weatherTest", it.toString())
            }.onFailure {
                Log.d("loadWeather", it.stackTraceToString())
            }
    }
}