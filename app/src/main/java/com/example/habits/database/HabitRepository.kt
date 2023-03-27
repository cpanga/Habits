package com.example.habits.database

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


//// TODO use this?
//class HabitRepository(private val database: HabitDatabase) {
//
//    val habits = MutableLiveData<List<Habit>>().apply {
//        postValue(database.habitDao().getAll())
//    }
//
//    suspend fun pullHabitList(): List<Habit> {
//        lateinit var habitList: List<Habit>
//        withContext(Dispatchers.IO) {
//            habitList = database.habitDao().getAll()
//        }
//        return habitList
//    }
//
//    suspend fun insertNewHabit(habit: Habit) {
//        withContext(Dispatchers.IO) {
//            database.habitDao().insertAll(habit)
//        }
//    }
//}