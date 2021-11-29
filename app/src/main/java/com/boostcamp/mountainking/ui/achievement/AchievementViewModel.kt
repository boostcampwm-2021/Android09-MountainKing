package com.boostcamp.mountainking.ui.achievement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.mountainking.data.RepositoryInterface
import com.boostcamp.mountainking.entity.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel() {

    private val _achievementListLiveData = MutableLiveData<List<Achievement>>()
    val achievementListLiveData: LiveData<List<Achievement>> get() = _achievementListLiveData

    private val _tabNameLiveData = MutableLiveData<TabName>()
    val tabNameLiveData: LiveData<TabName> get() = _tabNameLiveData

    private val _completedAchievementLiveData = MutableLiveData<Achievement>()
    val completedAchievementLiveData: LiveData<Achievement> get() = _completedAchievementLiveData

    private val _completeAchievementCount = MutableLiveData<Int>()
    val completeAchievementCount: LiveData<Int> get() = _completeAchievementCount

    private val _totalAchievementCount = MutableLiveData<Int>()
    val totalAchievementCount: LiveData<Int> get() = _totalAchievementCount

    private val _achievementScore = MutableLiveData<Int>()
    val achievementScore: LiveData<Int> get() = _achievementScore

    enum class TabName {
        TOTAL,
        COMPLETE,
        INCOMPLETE
    }

    private var achievementList = listOf<Achievement>()

    fun loadAchievementList() = viewModelScope.launch {
        achievementList = repository.getAchievement()
        _totalAchievementCount.value = achievementList.size
        _completeAchievementCount.value = achievementList.count { it.isComplete }

        val completeAchievementList = achievementList.filter { it.isComplete }
        _achievementScore.value =
            completeAchievementList.fold(0) { total, achievement -> total + achievement.score }
        updateAchievement()
        filterAchievementList()
    }

    fun filterAchievementList() {
        _achievementListLiveData.value = when (_tabNameLiveData.value) {
            TabName.TOTAL -> achievementList
            TabName.COMPLETE -> achievementList.filter { it.isComplete }
            TabName.INCOMPLETE -> achievementList.filter { !it.isComplete }
            null -> achievementList
        }
    }

    private fun updateAchievement() = viewModelScope.launch {
        val statistics = repository.getStatistics()
        val achievementList = repository.getAchievement()
        achievementList.forEach {
            if (it.progressAchievement(statistics)) {
                repository.updateAchievement(it)
                if (it.isComplete) {
                    _completedAchievementLiveData.value = it
                }
            }
        }
    }

    fun setTabName(tabName: TabName) {
        _tabNameLiveData.value = tabName
    }
}