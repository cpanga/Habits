package com.example.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    val fabVisibile = MutableLiveData<Boolean>().apply {postValue(true)}

    // State of each day of week button
    val monEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val tueEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val wedEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val thuEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val friEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val satEnabled = MutableLiveData<Boolean>().apply {postValue(false)}
    val sunEnabled = MutableLiveData<Boolean>().apply {postValue(false)}

    val reminderTime = MutableLiveData<String>().apply { postValue("9:00 AM") }
    val habitName = MutableLiveData<String>()
    val habitDesc = MutableLiveData<String>()
}