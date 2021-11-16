package com.boostcamp.mountainking.data

import androidx.room.Dao
import androidx.room.Query
import com.boostcamp.mountainking.entity.Mountain


@Dao
interface MountainDao {
    @Query("SELECT * FROM Mountain WHERE mountainName LIKE :search||'%'")
    fun searchMountainName(search: String): List<Mountain>

    @Query("SELECT * FROM Mountain WHERE locationName LIKE '%'||:state||'%' AND locationName LIKE '%'||:cityName||'%' AND mountainName LIKE '%'||:name||'%'")
    fun searchMountainNameInCity(state: String, cityName: String, name: String): List<Mountain>

    @Query("SELECT * FROM Mountain WHERE namedMountainDetails IS NOT NULL")
    fun searchNamedMountain(): List<Mountain>
}