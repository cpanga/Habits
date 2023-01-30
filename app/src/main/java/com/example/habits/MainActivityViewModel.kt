package com.example.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

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
        return ensureAtleastOneIsTrue(
            monEnabled,
            tueEnabled,
            wedEnabled,
            thuEnabled,
            friEnabled,
            satEnabled,
            sunEnabled
        )
    }

    private fun ensureAtleastOneIsTrue(vararg day: MutableLiveData<Boolean>): Boolean {
        for (day_ in day) {
            if (day_.value == true) return true
        }
        return false
    }
}