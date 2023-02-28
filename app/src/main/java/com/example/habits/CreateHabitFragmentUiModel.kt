package com.example.habits

import androidx.lifecycle.MutableLiveData

class CreateHabitFragmentUiModel {
    // State of each day of week button
    val monEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val tueEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val wedEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val thuEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val friEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val satEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val sunEnabled = MutableLiveData<Boolean>().apply {postValue(false)}

    val reminderTime = MutableLiveData<List<Int>>().apply { postValue(listOf(9,0)) }
    var streak:Int = 0
    val habitName = MutableLiveData<String>()
    val habitDesc = MutableLiveData<String>()
}