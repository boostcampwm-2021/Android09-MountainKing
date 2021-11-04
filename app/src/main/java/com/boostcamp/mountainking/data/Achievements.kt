package com.boostcamp.mountainking.data

import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType

fun getInitAchievementList(): List<Achievement> {
    return listOf(
        Achievement(
            id = 0,
            name = "지리산 반달곰",
            description = "지리산을 10회 이상 등산하세요",
            thumbnailUrl = "",
            type = AchievementType.MOUNTAIN_0_COUNT,
            curProgress = 0,
            maxProgress = 10,
            isComplete = false,
            completeDate = null,
            score = 100
        ),
        Achievement(
            id = 1,
            name = "콩밭 매는 아낙네",
            description = "칠갑산을 등산하세요",
            thumbnailUrl = "",
            type = AchievementType.MOUNTAIN_1_COUNT,
            curProgress = 0,
            maxProgress = 1,
            isComplete = false,
            completeDate = null,
            score = 10
        ),
        Achievement(
            id = 2,
            name = "학교세요?",
            description = "50회 이상 등산하세요",
            thumbnailUrl = "",
            type = AchievementType.TRACKING_COUNT,
            curProgress = 0,
            maxProgress = 50,
            isComplete = false,
            completeDate = null,
            score = 300
        ),
        Achievement(
            id = 3,
            name = "이방원의 환생",
            description = "만수산을 10회 이상 등산하세요",
            thumbnailUrl = "",
            type = AchievementType.MOUNTAIN_2_COUNT,
            curProgress = 0,
            maxProgress = 10,
            isComplete = false,
            completeDate = null,
            score = 100
        ),
    )
}