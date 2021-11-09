package com.boostcamp.mountainking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boostcamp.mountainking.entity.Tracking
import kotlinx.coroutines.CoroutineScope


@Database(entities = [Tracking::class], version = 1, exportSchema = false)
abstract class TrackingDatabase: RoomDatabase() {
    abstract fun trackingDao(): TrackingDao

    companion object{
        @Volatile
        private var INSTANCE: TrackingDatabase? = null

        fun getInstance(context: Context): TrackingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackingDatabase::class.java,
                    "tracking.db"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}