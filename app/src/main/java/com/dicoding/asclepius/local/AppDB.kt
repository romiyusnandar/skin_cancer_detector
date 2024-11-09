package com.dicoding.asclepius.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalResponse::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "result_db"
                ).build()
            }
        }
    }
}