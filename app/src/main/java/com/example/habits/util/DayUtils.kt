package com.example.habits.util

fun getStringFromDays(days: String): String {

    if (days == "1111111")  return "Every day"

    var str = ""

    if (days[0] == '1') str += "Mon "
    if (days[1] == '1') str += "Tue "
    if (days[2] == '1') str += "Wed "
    if (days[3] == '1') str += "Thu "
    if (days[4] == '1') str += "Fri "
    if (days[5] == '1') str += "Sat "
    if (days[6] == '1') str += "Sun "

    return str
}




