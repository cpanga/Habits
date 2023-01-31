package com.example.habits.database

import androidx.room.*

// TODO can't store lists in room DB - need another solution

@Entity
data class Habit(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "habit_name") val habitName: String,
        @ColumnInfo(name = "habit_desc") val habitDesc: String,
        @ColumnInfo(name = "days_of_week") val daysOfWeek: List<Boolean>,
        @ColumnInfo(name = "notif_hour") val notifHour: Int,
        @ColumnInfo(name = "notif_min") val notifMin: Int,
        @ColumnInfo(name = "streak") val streak: Int
)
