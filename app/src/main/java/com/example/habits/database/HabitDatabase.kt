package com.example.habits.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
@androidx.room.TypeConverters(Converters::class)
abstract class HabitDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}

private lateinit var INSTANCE: HabitDatabase

fun getDatabase(context: Context): HabitDatabase {
    synchronized(HabitDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                HabitDatabase::class.java,
                "habits").build()
        }
    }
    return INSTANCE
}