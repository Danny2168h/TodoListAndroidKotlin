package com.project.todolist

import com.project.todolist.screens.todolist.addMinutesToTime
import com.project.todolist.screens.todolist.formatDateString
import com.project.todolist.screens.todolist.isSelectionAfterToday
import com.project.todolist.screens.todolist.isTimeInFuture
import junit.framework.Assert.*
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class DateTimePickerMethodTest {

    lateinit var timeNow: ZonedDateTime

    @Before
    fun beforeEach() {
        timeNow = ZonedDateTime.now(TimeZone.getTimeZone("Canada/Pacific").toZoneId()).truncatedTo(
            ChronoUnit.MINUTES
        )
    }

    @Test
    fun isSelectionAfterTodaySelectDayInPast() {
        val dateTomorrow = timeNow.plusDays(1)
        assertFalse(
            isSelectionAfterToday(
                timeNow.dayOfMonth,
                timeNow.monthValue - 1,
                timeNow.year,
                dateTomorrow.dayOfMonth,
                dateTomorrow.monthValue - 1,
                dateTomorrow.year
            )
        )
    }

    @Test
    fun isSelectionAfterTodaySelectDayTheSame() {
        assertFalse(
            isSelectionAfterToday(
                timeNow.dayOfMonth, timeNow.monthValue - 1,
                timeNow.year, timeNow.dayOfMonth, timeNow.monthValue - 1, timeNow.year
            )
        )
    }

    @Test
    fun isSelectionAfterTodaySelectDayFuture() {
        val dateTomorrow = timeNow.plusDays(1)
        assertTrue(
            isSelectionAfterToday(
                dateTomorrow.dayOfMonth, dateTomorrow.monthValue - 1,
                dateTomorrow.year, timeNow.dayOfMonth, timeNow.monthValue - 1, timeNow.year
            )
        )
    }

    @Test
    fun isTimeInFutureSelectTimeInPast() {
        val futureTime = timeNow.plusMinutes(10)
        assertFalse(
            isTimeInFuture(
                timeNow.hour,
                timeNow.minute,
                0,
                futureTime.hour,
                futureTime.minute
            )
        )
    }

    @Test
    fun isTimeInFutureSelectTimeSame() {
        assertFalse(isTimeInFuture(timeNow.hour, timeNow.minute, 0, timeNow.hour, timeNow.minute))
    }

    @Test
    fun isTimeInFutureSelectTimeFuture() {
        val futureTime = timeNow.plusMinutes(10)
        assertTrue(
            isTimeInFuture(
                futureTime.hour,
                futureTime.minute,
                0,
                timeNow.hour,
                timeNow.minute
            )
        )
    }

    @Test
    fun addMinutesToTimeAdd0minutes() {
        val currHour = timeNow.hour
        val currMin = timeNow.minute
        val pairOutput = addMinutesToTime(currHour, currMin, 0)
        assertEquals(currHour, pairOutput.first)
        assertEquals(currMin, pairOutput.second)
    }

    @Test
    fun addMinutesToTimeAddTimeGoesOverHour() {
        val pairOutput = addMinutesToTime(5, 55, 10)
        assertEquals(6, pairOutput.first)
        assertEquals(5, pairOutput.second)
    }

    @Test
    fun addMinutesToTimeAddTimeStaysWithinHour() {
        val pairOutput = addMinutesToTime(5, 50, 1)
        assertEquals(5, pairOutput.first)
        assertEquals(51, pairOutput.second)
    }

    @Test
    fun formatDateStringTestBaseTextShows() {
        val baseText = "Test Text"
        assertEquals(baseText, formatDateString(-1, -1, -1, -1, -1, baseText))
    }

    @Test
    fun formatDateStringTestFormatIsProperAM() {
        val baseText = "Test Text"
        assertNotEquals(baseText, formatDateString(5, 5, 2022, 11, 59, baseText))
        assertEquals("5/Jun/2022 at 11:59 AM", formatDateString(5, 5, 2022, 11, 59, baseText))
    }

    @Test
    fun formatDateStringTestFormatIsProperPM() {
        val baseText = "Test Text"
        assertNotEquals(baseText, formatDateString(5, 5, 2022, 12, 0, baseText))
        assertEquals("5/Jun/2022 at 12:00 PM", formatDateString(5, 5, 2022, 12, 0, baseText))
    }
}