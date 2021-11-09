package com.boostcamp.mountainking.ui.tracking.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Tracking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel() {

    private val _historyList = MutableLiveData<List<Tracking>>()
    val historyList: LiveData<List<Tracking>> get() = _historyList

    fun getTrackingList() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO get Tracking data from database
        }
    }

}