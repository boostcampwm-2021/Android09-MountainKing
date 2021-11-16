package com.boostcamp.mountainking.ui.mountain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Mountain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainDetailViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainLiveData = MutableLiveData<Mountain?>()
    val mountainLiveData: LiveData<Mountain?> get() = _mountainLiveData

    fun getMountain(id: Int) =
        viewModelScope.launch {
            _mountainLiveData.value = repository.getMountain(id)
        }

}