package com.boostcamp.mountainking.ui.tracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MountainSelectViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainNameList = MutableLiveData<List<Mountain>>()
    val mountainNameList: LiveData<List<Mountain>> get() = _mountainNameList

    private val _dismiss = MutableLiveData<Event<Unit>>()
    val dismiss: LiveData<Event<Unit>> get() = _dismiss

    fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
        viewModelScope.launch {
            searchMountainName(sequence.toString())
        }
    }

    fun cancel() {
        _dismiss.value = Event(Unit)
    }

    private suspend fun searchMountainName(name: String) {
        withContext(viewModelScope.coroutineContext) {
            repository.searchMountainName(name)
        }.let {
            _mountainNameList.value = it
            Log.d("mountains", it.toString())
        }
    }

    fun setTrackingMountainName(mountainName: String) {
        repository.trackingMountain = mountainName
        cancel()
    }
}