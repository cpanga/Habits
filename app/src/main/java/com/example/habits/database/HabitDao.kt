package com.example.habits.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE habit_name LIKE :name LIMIT 1")
    fun findByName(name: String): Habit

    // Update all fields
    @Update
    fun updateHabit(habit: Habit)

    // Update only notifActive
    @Query("UPDATE habit SET streak=:streak WHERE uid = :id")
    fun updateStreak(streak: Int, id: Int)

    // Update only notifActive
    @Query("UPDATE habit SET notif_active=:notifActive WHERE uid = :id")
    fun updateNotif(notifActive: Int, id: Int)


    @Insert
    fun insertAll(vararg habits: Habit)

    @Query("DELETE FROM habit WHERE uid = :uid")
    fun deleteByUid(uid: Int)
}