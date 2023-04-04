package com.example.habits.util

import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout

/**
 * Post value of false to all LiveData arguments passed
 */
fun postDayValueFalse(vararg day: MutableLiveData<Boolean>) {
    for (day_ in day) {
        day_.postValue(false)
    }
}


/**
 * Ensure at least one value is equal to true from a set of MutableLiveData<Boolean>
 * @param day vararg parameter of MutableLiveData<Boolean>
 * @return return true if at least one input boolean value is true
 */
fun ensureAtLeastOneIsTrue(vararg day: MutableLiveData<Boolean>): Boolean {
    for (day_ in day) {
        if (day_.value == true) return true
    }
    return false
}

/**
 * @param textBox the text box
 * @return if the text box is empty
 */
fun isTextBoxEmpty(textBox: TextInputLayout): Boolean {
    return textBox.editText?.text?.trim()?.isEmpty() == true
}


/**
 * Convert the time from an hour and minute value to a written time
 * @param hour the time's hour
 * @param minute the time's minute
 * @return The time as a string with AM or PM
 */
fun convertTimeToString(hour: Int, minute: Int): String? {

    if (hour > 23 || hour < 0 || minute < 0 || minute > 59) return null

    val amOrPm = if (hour > 11) "PM" else "AM"
    val adjMin = if (minute < 10) "0$minute" else minute
    val adjHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour

    return "$adjHour:$adjMin $amOrPm"
}
