package com.boostcamp.mountainking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boostcamp.mountainking.entity.Mountain


@Database(
    entities = [Mountain::class], version = 1
)
abstract class MountainDatabase : RoomDatabase() {

    companion object {
        private var instance: MountainDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): MountainDatabase {
            synchronized(lock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        MountainDatabase::class.java,
                        "mountainInfo.db"
                    ).createFromAsset("mountainInfo.db")
                     .build()
                }
                return instance!!
            }
        }
    }
}