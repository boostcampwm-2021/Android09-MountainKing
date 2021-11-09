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
    private val _tabNameLiveData = MutableLiveData<TabName>()
    val tabNameLiveData: LiveData<TabName> get() = _tabNameLiveData

    enum class TabName {
        TOTAL,
        COMPLETE,
        INCOMPLETE
    }

    private var achievementList = listOf<Achievement>()
    private val statistics = Statistics()
    private val _statisticsLiveData = MutableLiveData<Statistics>()
    val statisticsLiveData: LiveData<Statistics> get() = _statisticsLiveData

    fun loadAchievementList() = viewModelScope.launch {
        achievementList = repository.getAchievement()
        filterAchievementList()
    }

    fun filterAchievementList() {
        _achievementListLiveData.value = when(_tabNameLiveData.value) {
            TabName.TOTAL -> achievementList
            TabName.COMPLETE -> achievementList.filter { it.isComplete }
            TabName.INCOMPLETE -> achievementList.filter { !it.isComplete }
            null -> achievementList
        }
    }

    fun updateAchievement() = viewModelScope.launch {
        achievementList.forEach {
            if (it.progressAchievement(statistics)){
                repository.updateAchievement(it)
            }
        }
        filterAchievementList()
    }

    fun increaseDistanceTest(){
        statistics.distance += 10
        statistics.time += 5
        _statisticsLiveData.value = statistics
    }

    fun setTabName(tabName: TabName) {
        _tabNameLiveData.value = tabName
    }
}