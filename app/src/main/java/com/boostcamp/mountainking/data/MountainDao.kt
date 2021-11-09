package com.boostcamp.mountainking.data

import androidx.room.Dao
import androidx.room.Query


@Dao
interface MountainDao {
    @Query("SELECT mountainName FROM Mountain WHERE mountainName LIKE :search||'%'")
    fun searchMountainName(search: String): List<String>
}