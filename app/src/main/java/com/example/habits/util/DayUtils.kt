package com.example.habits.util

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import java.util.logging.Logger

/**
 * Get a human readable string of which days are selected from a string of 0s and 1s which represent each day
 * @param days day string, which is 7 1s or 0s
 * @param logger for logging errors
 * @return human readable list of which days are active
 */
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

private fun addOneOrZero(day: Boolean, string: String): String {
    return string + if (day) "1" else "0"
}

fun getDayOfWeekFromUnixTime(time: Long): Int {
    return Calendar.getInstance().apply {
        timeInMillis = time
    }.get(Calendar.DAY_OF_WEEK)
}

fun shouldBeNotifiedToday(dayString: String, dayOfWeek: Int, logger: Logger): Boolean {
    if (!daysStringIsValid(dayString, logger)) return false
    // Calendar days are mapped to SUNDAY = 1 .. SATURDAY = 7, so reorder to start on Monday and index from 0
    val index = if (dayOfWeek == 1) 6 else dayOfWeek - 2
    return dayString[index] == '1'
}



