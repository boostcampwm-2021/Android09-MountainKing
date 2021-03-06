package com.boostcamp.mountainking.data

import android.util.Log
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import com.boostcamp.mountainking.entity.Mountain

fun getInitAchievementList(namedMountainList: List<Mountain>): List<Achievement> {
    Log.d("namedMountain", namedMountainList.size.toString())
    return listOf(
        Achievement(
            id = 0,
            name = "지리산 반달곰",
            description = "지리산을 10회 이상 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/bear.jpg?alt=media&token=5ba73073-233a-4252-9b74-ef1f18fb699e",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 10,
            isComplete = false,
            completeDate = null,
            score = 100,
            mountainIdList = listOf(1356)
        ),
        Achievement(
            id = 1,
            name = "콩밭 매는 아낙네",
            description = "칠갑산을 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/bean.jpg?alt=media&token=00fafb1f-7796-423d-91ba-e0a6638f3cbb",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 1,
            isComplete = false,
            completeDate = null,
            score = 10,
            mountainIdList = listOf(1456),
        ),
        Achievement(
            id = 2,
            name = "학교세요?",
            description = "50회 이상 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/school.jpg?alt=media&token=fdaddc1b-b647-4b9c-b51f-1431117fac20",
            type = AchievementType.TRACKING_TOTAL_COUNT,
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
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/leebangwon.PNG?alt=media&token=252b7077-0705-4a0a-a640-b21998e6e1f5",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 10,
            isComplete = false,
            completeDate = null,
            score = 100,
            mountainIdList = listOf(461)
        ),
        Achievement(
            id = 4,
            name = "산 넘어 산",
            description = "2일 안에 2번 이상 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/mountain_mountain.jpg?alt=media&token=fc860fcc-a15e-4320-94c9-fa3b947e067e",
            type = AchievementType.TRACKING_PERIOD_COUNT,
            curProgress = 0,
            maxProgress = 2,
            isComplete = false,
            completeDate = null,
            score = 50,
            period = 2,
        ),
        Achievement(
            id = 5,
            name = "흐에!",
            description = "기백산을 5회 이상 등산하세요",
            thumbnailUrl = "",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 5,
            isComplete = false,
            completeDate = null,
            score = 50,
            mountainIdList = listOf(257),
        ),
        Achievement(
            id = 6,
            name = "수헬리베붕탄질산",
            description = "화학산을 7회 이상 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/chemistry.png?alt=media&token=27642cdf-ebe5-4392-8a37-28726ef51d27",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 7,
            isComplete = false,
            completeDate = null,
            score = 70,
            mountainIdList = listOf(1565),
        ),
        Achievement(
            id = 7,
            name = "홍길동",
            description = "전체 등산 거리 100km 이상",
            thumbnailUrl = "",
            type = AchievementType.TRACKING_TOTAL_DISTANCE,
            curProgress = 0,
            maxProgress = 100,
            isComplete = false,
            completeDate = null,
            score = 100,
        ),
        Achievement(
            id = 8,
            name = "등산왕",
            description = "100대 명산을 10군데 이상 등산하세요",
            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/mountainking-d5724.appspot.com/o/mountain_king.png?alt=media&token=4189dec1-43ec-40ec-a27a-3fc35e8e4bdc",
            type = AchievementType.MOUNTAIN_KIND_COUNT,
            curProgress = 0,
            maxProgress = 10,
            isComplete = false,
            completeDate = null,
            score = 100,
            mountainIdList = namedMountainList.map { it.id },
        ),
        Achievement(
            id = 9,
            name = "환웅",
            description = "태백산 등산 100회 이상",
            thumbnailUrl = "",
            type = AchievementType.MOUNTAIN_COUNT,
            curProgress = 0,
            maxProgress = 100,
            isComplete = false,
            completeDate = null,
            score = 1000,
            mountainIdList = listOf(1478),
        ),
    )
}