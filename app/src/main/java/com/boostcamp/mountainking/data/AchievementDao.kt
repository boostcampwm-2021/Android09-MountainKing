package com.boostcamp.mountainking.data

import androidx.room.*
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Tracking

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(achievement: Achievement)

    @Query("SELECT * FROM Achievement ORDER BY id")
    fun getAchievementData(): List<Achievement>

    @Delete
    fun delete(achievement: Achievement)

    @Query("SELECT COUNT(*) FROM Achievement")
    fun countData(): Int

}