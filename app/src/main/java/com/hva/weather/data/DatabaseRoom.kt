package com.hva.weather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hva.weather.model.Locations
import com.hva.weather.model.Settings

@Database(entities = [Settings::class, Locations::class], version = 2, exportSchema = false)
abstract class DatabaseRoom : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseRoom? = null

        fun getDatabase(context: Context): DatabaseRoom? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseRoom::class.java, "database"
                )
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()

            }
            return INSTANCE
        }
    }
}