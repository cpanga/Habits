package com.example.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habits.database.Habit
import com.example.habits.database.HabitDao
import com.example.habits.util.postDayValueFalse
import com.example.habits.util.postDayValueFromString
import kotlinx.coroutines.flow.Flow

class HabitViewModel(private val dao: HabitDao) : ViewModel() {

    fun getAllHabits(): Flow<List<Habit>> = dao.getAll()
    fun updateHabit(habit: Habit) = dao.updateHabit(habit)
    fun deleteHabit(habit: Habit) = dao.delete(habit)

    // TODO decide how to use repository
    // private val habitRepository = HabitRepository(getDatabase(application))

    val fabVisible = MutableLiveData<Boolean>().apply { postValue(true) }

    // UI Model for holding data for CreateHabitFragment
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

    // Reset all
    fun resetCreateHabitFragmentUiModel() {
            // Reset the new habit form values to the default
            postDayValueFalse(
                _createHabitUiModel.monEnabled,
                _createHabitUiModel.tueEnabled,
                _createHabitUiModel.wedEnabled,
                _createHabitUiModel.thuEnabled,
                _createHabitUiModel.friEnabled,
                _createHabitUiModel.satEnabled,
                _createHabitUiModel.sunEnabled,
                _createHabitUiModel.nameTextInteractedWith,
                _createHabitUiModel.descTextInteractedWith
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
    class HabitViewModelFactory(private val habitDao: HabitDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HabitViewModel(habitDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}