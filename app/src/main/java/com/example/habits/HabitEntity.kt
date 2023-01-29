package com.example.habits

import androidx.room.*

class HabitEntity {

    data class Habit(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "habit_name") val habitName: String?,
        @ColumnInfo(name = "habit_desc") val habitDesc: String?,
        @ColumnInfo(name = "days_of_week") val daysOfWeek: List<Boolean>?,
        @ColumnInfo(name = "notif_time") val notifTime: Int?,
        @ColumnInfo(name = "streak") val streak: Int?
    )
}