package com.boostcamp.mountainking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.util.Converters


@Database(
    entities = [Statistics::class, Achievement::class, Tracking::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao
    abstract fun statisticsDao(): StatisticsDao
    abstract fun trackingDao(): TrackingDao

    companion object {
        private const val DATABASE_NAME = "DB"
        private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Tracking ADD COLUMN steps INTEGER DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    UPDATE Achievement
                    SET thumbnailUrl = ""
                """.trimIndent())
            }
        }

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
                }
            }
            return instance!!
        }

    }

}