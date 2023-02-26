package com.example.habits.util

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.habits.database.Habit

fun Habit.toBundle(): Bundle {
    return bundleOf(
        "name" to habitName,
        "desc" to habitDesc,
        "notif_hour" to notifHour,
        "notif_min" to notifMin,
        "streak" to streak,
        "days_of_week" to daysOfWeek
    )
}