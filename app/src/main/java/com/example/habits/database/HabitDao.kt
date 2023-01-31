package com.example.habits.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): List<Habit>

    @Query("SELECT * FROM habit WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Habit>

    @Query("SELECT * FROM habit WHERE habit_name LIKE :name LIMIT 1")
    fun findByName(name: String): Habit

    @Insert
    fun insertAll(vararg habits: Habit)

    @Delete
    fun delete(habit: Habit)
}