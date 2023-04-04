package com.example.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habits.database.Habit
import com.example.habits.database.HabitDao
import com.example.habits.util.postDayValueFalse
import com.example.habits.util.postDayValueFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HabitViewModel(private val dao: HabitDao) : ViewModel() {

    fun getAllHabits(): Flow<List<Habit>> = dao.getAll()
    suspend fun updateHabit(habit: Habit) = withContext(Dispatchers.IO) { dao.updateHabit(habit) }
    suspend fun deleteHabit(uid: Int) = withContext(Dispatchers.IO) { dao.deleteByUid(uid) }
    suspend fun createHabit(habit: Habit) = withContext(Dispatchers.IO) { dao.insertAll(habit) }

    val fabVisible = MutableLiveData<Boolean>().apply { postValue(true) }
    val welcomeScreenVisible = MutableLiveData<Boolean>().apply { postValue(true) }

    // UI Model for holding data for CreateHabitFragment
    private val _createHabitUiModel = CreateHabitFragmentUiModel()
    val createHabitUiModel: CreateHabitFragmentUiModel
        get() = _createHabitUiModel

    fun populateUiModel(habit: Habit) {
        _createHabitUiModel.run {
            uid = habit.uid
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
            uid = null
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