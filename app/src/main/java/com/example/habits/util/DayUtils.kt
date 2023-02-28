package com.example.habits.util

import com.example.habits.MainActivity.Companion.log

fun getStringFromDays(days: String): String {

    val dayList = listOf("Mon ", "Tue ", "Wed ", "Thu ", "Fri ", "Sat ", "Sun " )

    // TODO do something better here
    if(!daysStringIsValid(days)) return "Error"

    if (days == "1111111")  return "Every day"

    var str = ""
    for (i in 0..6) {
        if (days[i] =='1') str += dayList[i]
    }

    return str
}

/**
 * Convert the time from an hour and minute value to a written time
 * @param hour the time's hour
 * @param minute the time's minute
 * @return The time as a string with AM or PM
 */
fun convertTimeToString(hour: Int, minute:Int): String {
    val amOrPm = if (hour>11) "PM" else "AM"
    val adjHour = if (hour>12) hour-12 else hour
    val adjMin = if (minute<10) "0$minute" else minute

    return "$adjHour:$adjMin $amOrPm"
}

/**
 * Confirm that the days string is 7 digits long, and only contains 1s or 0s.
 */
fun daysStringIsValid(days: String): Boolean {

    if(days.length != 7)  {
        log.warning("More than 7 days present in the day")
        return false
    }

    if (days.matches("[^0-1]".toRegex())) {
        log.warning("days string contains characters other than 0 or 1")
        return false
    }
     return true
}




