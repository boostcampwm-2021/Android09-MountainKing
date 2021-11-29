package com.boostcamp.mountainking.data

import androidx.room.*
import com.boostcamp.mountainking.entity.Achievement

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

    @Query("UPDATE Achievement SET thumbnailUrl = :thumbnailUrl WHERE id = :id")
    fun updateThumbnailUrl(thumbnailUrl: String, id: Long)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

}