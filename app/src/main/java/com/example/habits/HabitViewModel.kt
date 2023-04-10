package com.example.habits

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.habits.database.Habit
import com.example.habits.database.HabitDao
import com.example.habits.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.Logger

class HabitViewModel(private val dao: HabitDao) : ViewModel() {
    companion object {
        val log: Logger = Logger.getLogger(HabitViewModel::class.java.name)
    }

    fun getAllHabits(): Flow<List<Habit>> = dao.getAll()
    suspend fun updateHabit(habit: Habit) = withContext(Dispatchers.IO) { dao.updateHabit(habit) }
    private suspend fun updateStreak(streak: Int, uid: Int) =
        withContext(Dispatchers.IO) { dao.updateStreak(streak, uid) }

    private suspend fun updateNotifActive(notifActive: Int, uid: Int) =
        withContext(Dispatchers.IO) { dao.updateNotif(notifActive, uid) }

    private suspend fun updateLastNotified(lastNotified: Int, uid: Int) =
        withContext(Dispatchers.IO) { dao.updateLastNotified(lastNotified, uid) }

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
            notifActive = habit.notifActive
            lastNotified = habit.lastNotified
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

    /**
     *  Handle checking if the habit should be active today, and updating if so
     */
    fun checkHabitActiveState(habit: Habit) {
        val time = Calendar.getInstance().timeInMillis
        val dayOfWeek = getDayOfWeekFromUnixTime(time)
        // For the notification to activate, all of the following must be true:
        // 1. The day of the week must match one of the days designated in the habit
        // 2. The notification active must not already be true
        // 3. The day when the habit was last checked for activation was not today
        if (shouldBeNotifiedToday(
                habit.daysOfWeek,
                dayOfWeek,
                log
            ) && habit.notifActive != 1 && habit.lastNotified != dayOfWeek
        )
            viewModelScope.launch {
                log.info("Updating active state for ${habit.habitName}")
                updateLastNotified(dayOfWeek, habit.uid)
                updateNotifActive(1, habit.uid)
            }
    }

    /**
     *  Update habit active state for all habits
     */
    fun checkHabitActiveForAllHabits() {
        viewModelScope.launch {
            getAllHabits().collect() {
                it.forEach { habit ->
                    checkHabitActiveState(habit)
                }
            }
        }
    }

    /**
     *  Handle User pressing 'Done' on an active habit
     */
    fun habitDonePressed(habit: Habit) {
        val uid = habit.uid
        val newStreak = habit.streak + 1
        // Only update if notifActive is true
        if (habit.notifActive == 1) {
            log.info("Updating streak for ${habit.habitName} to $newStreak")
            viewModelScope.launch {
                updateNotifActive(0, uid)
                updateStreak(newStreak, uid)
            }
        } else {
            log.warning("Pressed done when notifActive was not 1 - shouldn't happen")
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
            notifActive = 0
            // Set to 9 as a default value
            lastNotified = 9
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