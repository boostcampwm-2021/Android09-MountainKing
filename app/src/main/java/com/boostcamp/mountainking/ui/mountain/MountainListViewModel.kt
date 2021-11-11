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
class MountainListViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainNameList = MutableLiveData<List<Mountain>?>()
    val mountainNameList: LiveData<List<Mountain>?> get() = _mountainNameList

    fun searchMountainNameInCity(state: String, cityName: String, name: String) =
        viewModelScope.launch {
            _mountainNameList.postValue(repository.searchMountainNameInCity(state, cityName, name))
        }

}