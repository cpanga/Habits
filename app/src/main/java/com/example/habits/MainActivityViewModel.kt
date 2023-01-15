package com.example.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    val fabVisibile = MutableLiveData<Boolean>().apply {postValue(true)}
}