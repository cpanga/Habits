package com.example.habits

import android.app.Application
import com.example.habits.database.HabitDatabase

// Used in the manifest to allow database access
class HabitApplication : Application() {
    val database: HabitDatabase by lazy { HabitDatabase.getDatabase(this) }
}