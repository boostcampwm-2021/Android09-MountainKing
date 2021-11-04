package com.boostcamp.mountainking.ui.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _achievementListLiveData = MutableLiveData<List<Achievement>>()
    val achievementListLiveData: LiveData<List<Achievement>> get() = _achievementListLiveData

    private var achievementList = listOf<Achievement>()

    fun loadAchievementList() = viewModelScope.launch {
        achievementList = repository.getAchievement().getOrNull() ?: listOf()
        _achievementListLiveData.value = achievementList
    }

    fun filterAchievementList(filter: Boolean? = null) {
        _achievementListLiveData.value = filter?.let {
            achievementList.filter { it.isComplete == filter }
        } ?: achievementList
    }
}