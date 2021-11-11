package com.boostcamp.mountainking.data

import androidx.room.*

@Dao
interface StatisticsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(statistics: Statistics)

    @Update
    fun update(statistics: Statistics)

    @Delete
    fun delete(statistics: Statistics)

    @Query("UPDATE Statistics SET distance = distance + :distance, time = time + :time, mountainMap = :mountainMap")
    fun update(distance: Int, time: Int, mountainMap: Map<Int, Int>)

    @Query("SELECT * FROM Statistics")
    fun getStatistics(): Statistics
}