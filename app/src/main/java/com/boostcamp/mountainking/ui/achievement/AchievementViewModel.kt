package com.boostcamp.mountainking.ui.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.Repository
import com.boostcamp.mountainking.data.Statistics
import com.boostcamp.mountainking.entity.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _achievementListLiveData = MutableLiveData<List<Achievement>>()
    val achievementListLiveData: LiveData<List<Achievement>> get() = _achievementListLiveData

    private var achievementList = listOf<Achievement>()
    private val statistics = Statistics()
    private val _statisticsLiveData = MutableLiveData<Statistics>()
    val statisticsLiveData: LiveData<Statistics> get() = _statisticsLiveData

    fun loadAchievementList() = viewModelScope.launch {
        achievementList = repository.getAchievement()
        _achievementListLiveData.value = achievementList
    }

    fun filterAchievementList(filter: Boolean? = null) {
        _achievementListLiveData.value = filter?.let {
            achievementList.filter { it.isComplete == filter }
        } ?: achievementList
    }

    fun updateAchievement() {
        achievementList.forEach {
            it.progressAchievement(statistics)
        }
        _achievementListLiveData.value = achievementList
    }

    fun increaseDistanceTest(){
        statistics.distance += 10
        statistics.time += 5
        _statisticsLiveData.value = statistics
    }
}