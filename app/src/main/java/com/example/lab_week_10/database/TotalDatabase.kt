package com.example.lab_week_10.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Update the version when you change the schema
@Database(entities = [Total::class], version = 1, exportSchema = false)
abstract class TotalDatabase : RoomDatabase() {

    // Declare the Dao
    abstract fun totalDao(): TotalDao

    companion object {
        @Volatile
        private var INSTANCE: TotalDatabase? = null

        fun getDatabase(context: Context): TotalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TotalDatabase::class.java,
                    "total_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
