package com.example.habits.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    // Gets all habits, ordering by notif_active such that habits that can be marked done appear first
    @Query("SELECT * FROM habit ORDER BY notif_active DESC")
    fun getAll(): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE habit_name LIKE :name LIMIT 1")
    fun findByName(name: String): Habit

    // Update all fields
    @Update
    fun updateHabit(habit: Habit)

    // Update only streak
    @Query("UPDATE habit SET streak=:streak WHERE uid = :id")
    fun updateStreak(streak: Int, id: Int)

    // Update only lastNotified
    @Query("UPDATE habit SET last_notified=:lastNotified WHERE uid = :id")
    fun updateLastNotified(lastNotified: Int, id: Int)

    // Update only notifActive
    @Query("UPDATE habit SET notif_active=:notifActive WHERE uid = :id")
    fun updateNotif(notifActive: Int, id: Int)


    @Insert
    fun insertAll(vararg habits: Habit)

    @Query("DELETE FROM habit WHERE uid = :uid")
    fun deleteByUid(uid: Int)
}