package com.boostcamp.mountainking.ui.tracking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Mountain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MountainSelectViewModel @Inject constructor(private val repository: RepositoryInterface) :
    ViewModel() {

    private val _mountainNameList = MutableLiveData<List<Mountain>>()
    val mountainNameList: LiveData<List<Mountain>> get() = _mountainNameList

    fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
        viewModelScope.launch {
            searchMountainName(sequence.toString())
        }
    }

    private suspend fun searchMountainName(name: String) {
        withContext(viewModelScope.coroutineContext) {
            repository.searchMountainName(name)
        }.let {
            _mountainNameList.value = it
            Log.d("mountains", it.toString())
        }
    }
}