package com.boostcamp.mountainking.data

import androidx.room.*
import com.boostcamp.mountainking.entity.Tracking

@Dao
interface TrackingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tracking: Tracking)

    @Query("SELECT * FROM Tracking ORDER BY id")
    fun getTrackingData(): List<Tracking>

    @Delete
    suspend fun delete(tracking: Tracking)
}