package com.example.habits.util

import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import java.util.logging.Logger
import kotlin.math.min

fun getStringFromDays(days: String, logger: Logger): String? {

    val dayList = listOf("Mon ", "Tue ", "Wed ", "Thu ", "Fri ", "Sat ", "Sun ")

    if (!daysStringIsValid(days, logger)) return null

    if (days == "1111111") return "Every day"

    var str = ""
    for (i in 0..6) {
        if (days[i] == '1') str += dayList[i]
    }

    return str
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

/**
 * Confirm that the days string is 7 digits long, and only contains 1s or 0s
 * @param days string representing which days the user has selected to
 */
fun daysStringIsValid(days: String, logger: Logger): Boolean {

    if (days.length != 7) {
        logger.warning("${days.length} days in the day string! Should be 7.")
        return false
    }

    if (!days.matches("^[01]+\$".toRegex())) {
        logger.warning("days string contains characters other than 0 or 1!")
        return false
    }
    logger.info("day string $days is valid!")
    return true
}

/**
 * Post value of false to all LiveData arguments passed
 */
fun postDayValueFalse(vararg day: MutableLiveData<Boolean>) {
    for (day_ in day) {
        day_.postValue(false)
    }
}

/**
 * Parse a days string and update LiveData to match
 */
fun postDayValueFromString(
    vararg day: MutableLiveData<Boolean>,
    daysString: String,
    logger: Logger
) {
    if (daysString.length != 7) logger.info("Not passed in 7 days - days length is ${daysString.length}")
    else {
        for ((index, day_) in day.withIndex()) {
            day_.postValue(daysString[index] == '1')
        }
    }
}

/**
 * Convert a list of Booleans in to a days string
 */
fun convertDaysToString(days: List<Boolean>, logger: Logger): String {
    if (days.size != 7) logger.info("Not passed in 7 days - days size is ${days.size}. Do something about this!")
    var dayString = ""
    for (day in days) {
        dayString = addOneOrZero(day, dayString)
    }
    return dayString
}

private fun addOneOrZero(day: Boolean, dayString: String): String {
    return dayString + if (day) "1" else "0"
}


fun ensureAtLeastOneIsTrue(vararg day: MutableLiveData<Boolean>): Boolean {
    for (day_ in day) {
        if (day_.value == true) return true
    }
    return false
}

fun isTextBoxEmpty(textBox: TextInputLayout): Boolean {
    return textBox.editText?.text?.trim()?.isEmpty() == true
}




