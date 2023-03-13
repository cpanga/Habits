package com.example.habits

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habits.database.Habit
import com.example.habits.util.postDayValueFalse
import com.example.habits.util.postDayValueFromString

class MainActivityViewModel(application: Application) : ViewModel() {

//    private val habitRepository = HabitRepository(getDatabase(application))

    val fabVisible = MutableLiveData<Boolean>().apply { postValue(true) }

    private val _createHabitUiModel = CreateHabitFragmentUiModel()

    val createHabitUiModel: CreateHabitFragmentUiModel
        get() = _createHabitUiModel

    fun populateUiModel(habit: Habit) {
        _createHabitUiModel.run {
            habitName.postValue(habit.habitName)
            habitDesc.postValue(habit.habitDesc)
            streak = habit.streak
            reminderTime.postValue(listOf(habit.notifHour, habit.notifMin))
            postDayValueFromString(
                _createHabitUiModel.monEnabled,
                _createHabitUiModel.tueEnabled,
                _createHabitUiModel.wedEnabled,
                _createHabitUiModel.thuEnabled,
                _createHabitUiModel.friEnabled,
                _createHabitUiModel.satEnabled,
                _createHabitUiModel.sunEnabled,
                daysString = habit.daysOfWeek,
                logger = CreateHabitFragment.log
            )
        }
    }

    fun resetCreateHabitFragmentUiModel() {
            // Reset the new habit form values to the default
            postDayValueFalse(
                _createHabitUiModel.monEnabled,
                _createHabitUiModel.tueEnabled,
                _createHabitUiModel.wedEnabled,
                _createHabitUiModel.thuEnabled,
                _createHabitUiModel.friEnabled,
                _createHabitUiModel.satEnabled,
                _createHabitUiModel.sunEnabled
            )
        _createHabitUiModel.run {
            reminderTime.postValue((listOf(9, 0)))
            habitDesc.postValue("")
            habitName.postValue("")
            streak = 0
        }
    }

    /**
     * Factory for constructing Viewmodel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}