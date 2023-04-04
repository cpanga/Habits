package com.example.habits

import com.example.habits.util.convertTimeToString
import com.example.habits.util.getStringFromDays
import com.example.habits.util.shouldBeNotifiedToday
import org.junit.Assert.*
import org.junit.Test
import java.util.logging.Logger

class UtilsTest {
    companion object {
        val log: Logger = Logger.getLogger(MainActivity::class.java.name)
    }

    @Test
    fun test_get_string_from_days() {
        // Test valid strings
        val everyDay = "1111111"
        assertEquals("Every day", getStringFromDays(everyDay, log))
        val monWedFri = "1010100"
        assertEquals("Mon Wed Fri ", getStringFromDays(monWedFri, log))
        val satSun = "0000011"
        assertEquals("Sat Sun ", getStringFromDays(satSun, log))
        val none = "0000000"
        assertEquals("", getStringFromDays(none, log))
        val tueWedThu = "0111000"
        assertEquals("Tue Wed Thu ", getStringFromDays(tueWedThu, log))

        // Test invalid strings
        val tooShort = "10110"
        assertNull(getStringFromDays(tooShort, log))
        val tooLong = "101101010100101"
        assertNull(getStringFromDays(tooLong, log))
        val containsLetters = "jdnakds"
        assertNull(getStringFromDays(containsLetters, log))
        val containsOtherNums = "1234567"
        assertNull(getStringFromDays(containsOtherNums, log))
    }

    @Test
    fun test_convert_time_to_string() {

        // Test valid values:
        assertEquals("2:02 PM", convertTimeToString(14, 2))
        assertEquals("11:59 PM", convertTimeToString(23, 59))
        assertEquals("12:01 AM", convertTimeToString(0, 1))
        assertEquals("12:34 PM", convertTimeToString(12, 34))
        assertEquals("3:11 PM", convertTimeToString(15, 11))
        assertEquals("7:09 AM", convertTimeToString(7, 9))


        // Test errors:
        assertNull(convertTimeToString(-1, 0))
        assertNull(convertTimeToString(0, -1))
        assertNull(convertTimeToString(0, 60))
        assertNull(convertTimeToString(24, 0))
    }

    @Test
    fun test_should_be_notified() {
        val tueTime = 1294713217000
        val wedsTime = 821241217000
        val friTime = 1168569217000
        val sunTime = 1295145217000

        val everyDayString = "1111111"
        val noDayString = "0000000"
        val tueAndSun = "0100001"

        // All should be false for noDayString
        assertFalse(shouldBeNotifiedToday(noDayString, tueTime, log))
        assertFalse(shouldBeNotifiedToday(noDayString, wedsTime, log))
        assertFalse(shouldBeNotifiedToday(noDayString, friTime, log))
        assertFalse(shouldBeNotifiedToday(noDayString, sunTime, log))

        // All should be true for everyDayString
        assertTrue(shouldBeNotifiedToday(everyDayString, tueTime, log))
        assertTrue(shouldBeNotifiedToday(everyDayString, wedsTime, log))
        assertTrue(shouldBeNotifiedToday(everyDayString, friTime, log))
        assertTrue(shouldBeNotifiedToday(everyDayString, sunTime, log))


        // Only Tuesday and Sunday should be true for tueAndSun
        assertTrue(shouldBeNotifiedToday(tueAndSun, tueTime, log))
        assertFalse(shouldBeNotifiedToday(tueAndSun, wedsTime, log))
        assertFalse(shouldBeNotifiedToday(tueAndSun, friTime, log))
        assertTrue(shouldBeNotifiedToday(tueAndSun, sunTime, log))

    }
}