package com.boostcamp.mountainking.data

import androidx.room.Dao
import androidx.room.Query
import com.boostcamp.mountainking.entity.Mountain


@Dao
interface MountainDao {
    @Query("SELECT * FROM Mountain WHERE mountainName LIKE :search||'%'")
    fun searchMountainName(search: String): List<Mountain>
}