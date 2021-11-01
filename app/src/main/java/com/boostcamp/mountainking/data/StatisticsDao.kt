package com.boostcamp.mountainking.data

import androidx.room.*

@Dao
interface StatisticsDao {
    @Insert(onConflict=OnConflictStrategy.IGNORE)
    fun insert(statistics: Statistics)

    @Update
    fun update(statistics: Statistics)

    @Delete
    fun delete(statistics: Statistics)
}