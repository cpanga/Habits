package com.example.habits

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModel(application: Application): ViewModel() {

//    private val habitRepository = HabitRepository(getDatabase(application))

    val fabVisible = MutableLiveData<Boolean>().apply {postValue(true)}

    fun postDayValueFalse(vararg day: MutableLiveData<Boolean>) {
        for (day_ in day) {
            day_.postValue(false)
        }
    }

    fun postDayValueFromString(vararg day: MutableLiveData<Boolean>, daysString: String) {
        if (daysString.length!=7) println("Not passed in 7 days - days length is ${daysString.length}")
        else {
            for ((index,day_) in day.withIndex()) {
                day_.postValue(daysString[index] == '1')
            }
        }
    }


    fun moreThanZeroDaysSelected(
        mon: MutableLiveData<Boolean>,
        tue: MutableLiveData<Boolean>,
        wed: MutableLiveData<Boolean>,
        thu: MutableLiveData<Boolean>,
        fri: MutableLiveData<Boolean>,
        sat: MutableLiveData<Boolean>,
        sun: MutableLiveData<Boolean>
    ): Boolean{
        return ensureAtLeastOneIsTrue(
            mon,
            tue,
            wed,
            thu,
            fri,
            sat,
            sun
        )
    }

    private fun ensureAtLeastOneIsTrue(vararg day: MutableLiveData<Boolean>): Boolean {
        for (day_ in day) {
            if (day_.value == true) return true
        }
        return false
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