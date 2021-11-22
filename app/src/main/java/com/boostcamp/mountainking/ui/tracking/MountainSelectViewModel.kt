package com.boostcamp.mountainking.ui.tracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.util.Event
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MountainSelectViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainNameList = MutableLiveData<List<Mountain>?>()
    val mountainNameList: LiveData<List<Mountain>?> get() = _mountainNameList

    private val _dismiss = MutableLiveData<Event<Unit>>()
    val dismiss: LiveData<Event<Unit>> get() = _dismiss

    fun cancel() {
        _dismiss.value = Event(Unit)
    }

    fun searchMountainName(name: String, location: LatLng? = null) = viewModelScope.launch {
        _mountainNameList.postValue(repository.searchMountainName(name, location))
    }

    fun setTrackingMountain(mountainName: String, id: Int) {
        repository.trackingMountain = mountainName
        repository.trackingMountainID = id
        cancel()
    }
}