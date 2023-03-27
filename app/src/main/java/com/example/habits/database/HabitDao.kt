package com.example.habits.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE habit_name LIKE :name LIMIT 1")
    fun findByName(name: String): Habit

    @Update
    fun updateHabit(habit: Habit)

    @Insert
    fun insertAll(vararg habits: Habit)

    @Delete
    fun delete(habit: Habit)
}