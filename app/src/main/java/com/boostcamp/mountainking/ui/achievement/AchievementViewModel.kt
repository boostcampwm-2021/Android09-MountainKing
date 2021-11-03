package com.boostcamp.mountainking.ui.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import java.util.*

class AchievementViewModel : ViewModel() {

    private val _achievementListLiveData = MutableLiveData<List<Achievement>>()
    val achievementListLiveData: LiveData<List<Achievement>> get() = _achievementListLiveData
    val achievement = Achievement(
        "1",
        "불암산 정복자",
        "불암산 등산",
        "",
        AchievementType.TRACKING_COUNT,
        1,
        2,
        false,
        Date(),
        30
    )

    val achievement2 = Achievement(
        "2",
        "한라산 정복자",
        "한라산 등산",
        "",
        AchievementType.TRACKING_COUNT,
        2,
        2,
        true,
        Date(),
        300
    )

    var achievementList = listOf(achievement, achievement2).sortedBy { !it.isComplete }

    fun loadAchievementList(filter: Boolean? = null) {
        _achievementListLiveData.value = filter?.let {
            achievementList.filter { it.isComplete == filter }
        } ?: achievementList
    }
}