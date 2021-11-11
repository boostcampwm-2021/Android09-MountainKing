package com.boostcamp.mountainking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.util.Converters

@Database(
    entities = [Achievement::class], version = 1
)
@TypeConverters(Converters::class)
abstract class AchievementDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao

    companion object {
        fun provideAchievementDao(context: Context): AchievementDao =
            getInstance(context).achievementDao()

        private var INSTANCE: AchievementDatabase? = null

        private val lock = Any()

        private fun getInstance(context: Context): AchievementDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AchievementDatabase::class.java,
                        "achievement.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }

}