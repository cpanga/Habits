package com.example.habits

import androidx.lifecycle.MutableLiveData
import com.example.habits.database.Habit
import com.example.habits.util.convertDaysToString
import java.util.logging.Logger

class CreateHabitFragmentUiModel {

    var uid: Int? = null

    // State of each day of week button
    val monEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val tueEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val wedEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val thuEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val friEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val satEnabled = MutableLiveData<Boolean>().apply { postValue(false) }
    val sunEnabled = MutableLiveData<Boolean>().apply { postValue(false) }

    val reminderTime = MutableLiveData<List<Int>>().apply { postValue(listOf(9, 0)) }
    var streak: Int = 0
    val habitName = MutableLiveData<String>()
    val habitDesc = MutableLiveData<String>()

    // Track if text boxes have been interacted with
    val nameTextInteractedWith = MutableLiveData<Boolean>().apply { postValue(false) }
    val descTextInteractedWith = MutableLiveData<Boolean>().apply { postValue(false) }


    // TODO adapt to edit existing habit when editing
    fun returnAsHabit(): Habit {
        val listOfDays = listOf(
            monEnabled.value ?: true,
            tueEnabled.value ?: true,
            wedEnabled.value ?: true,
            thuEnabled.value ?: true,
            friEnabled.value ?: true,
            satEnabled.value ?: true,
            sunEnabled.value ?: true,
        )
        return Habit(
            uid = uid ?: 0,
            habitName = habitName.value ?: "Habit Name",
            habitDesc = habitDesc.value ?: "Habit Desc",
            streak = streak,
            notifHour = reminderTime.value?.first() ?: 9,
            notifMin = reminderTime.value?.last() ?: 0,
            daysOfWeek = convertDaysToString(
                listOfDays,
                Logger.getLogger(CreateHabitFragment::class.java.name)
            )
        )
    }
}