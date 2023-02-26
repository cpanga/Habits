package com.example.habits

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habits.database.HabitRepository
import com.example.habits.database.getDatabase

class MainActivityViewModel(application: Application): ViewModel() {

//    private val habitRepository = HabitRepository(getDatabase(application))

    val fabVisible = MutableLiveData<Boolean>().apply {postValue(true)}

    // State of each day of week button
    val monEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val tueEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val wedEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val thuEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val friEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val satEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val sunEnabled = MutableLiveData<Boolean>().apply {postValue(false)}

    val reminderTime = MutableLiveData<List<Int>>().apply { postValue(listOf(9,0)) }
    val habitName = MutableLiveData<String>()
    val habitDesc = MutableLiveData<String>()

    fun resetValues() {
        // Reset the new habit form values to the default
        postDayValueFalse(
            monEnabled,
            tueEnabled,
            wedEnabled,
            thuEnabled,
            friEnabled,
            satEnabled,
            sunEnabled
        )
        reminderTime.postValue((listOf(9,0)))
        habitDesc.postValue("")
        habitName.postValue("")
    }

    private fun postDayValueFalse(vararg day: MutableLiveData<Boolean>) {
        for (day_ in day) {
            day_.postValue(false)
        }
    }

    fun moreThanZeroDaysSelected(): Boolean{
        return ensureAtLeastOneIsTrue(
            monEnabled,
            tueEnabled,
            wedEnabled,
            thuEnabled,
            friEnabled,
            satEnabled,
            sunEnabled
        )
    }

    private fun ensureAtLeastOneIsTrue(vararg day: MutableLiveData<Boolean>): Boolean {
        for (day_ in day) {
            if (day_.value == true) return true
        }
        return false
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
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