package ru.itmo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class EventStatisticsTest {

    private companion object {
        const val EVENT_ONE = "event1"
        const val EVENT_TWO = "event2"

        const val HOUR_MINUTES = 60
        const val EPS = 1e-6
    }

    private lateinit var testClock: TestClock

    private lateinit var eventsStatistics: EventsStatistics

    @BeforeEach
    fun prepareTest() {
        testClock = TestClock(Instant.now())
        eventsStatistics = EventsStatisticsImpl(testClock)
    }

    @Test
    fun `checking that all events have been added successfully`() {
        repeat(HOUR_MINUTES) {  eventsStatistics.incEvent(EVENT_ONE) } // rms = 1
        repeat(HOUR_MINUTES * 2) { eventsStatistics.incEvent(EVENT_TWO) } // rms = 2

        checkEvent(EVENT_ONE, 1.0)
        checkEvent(EVENT_TWO, 2.0)
    }

    @Test
    fun `checking that outdated events have been deleted`() {
        repeat(HOUR_MINUTES) { eventsStatistics.incEvent(EVENT_ONE) }

        testClock.instant = testClock
            .instant
            .plus(2, ChronoUnit.HOURS)

        repeat(HOUR_MINUTES) { eventsStatistics.incEvent(EVENT_ONE) }

        checkEvent(EVENT_ONE, 1.0)
    }

    @Test
    fun `checking that all outdated events have been deleted`() {
        repeat(HOUR_MINUTES) {  eventsStatistics.incEvent(EVENT_ONE) }
        repeat(HOUR_MINUTES * 2) { eventsStatistics.incEvent(EVENT_TWO) }

        testClock.instant = testClock
            .instant
            .plus(2, ChronoUnit.HOURS)

        checkEvent(EVENT_ONE, 0.0)
        checkEvent(EVENT_TWO, 0.0)
    }

    @Test
    fun `checking that events will be taken into account at different times`() {
        for (i in listOf(10, 20, 30)) {
            testClock.instant = testClock
                .instant
                .plus(10, ChronoUnit.MINUTES)

            repeat(HOUR_MINUTES) {  eventsStatistics.incEvent(EVENT_ONE) }
        }

        checkEvent(EVENT_ONE, 3.0)
    }

    @Test
    fun `checking full circuit handle events`() {
        for (i in listOf(10, 20, 30, 40)) {
            testClock.instant = testClock
                .instant
                .plus(10, ChronoUnit.MINUTES)

            repeat(HOUR_MINUTES) {  eventsStatistics.incEvent(EVENT_ONE) }
        }

        checkEvent(EVENT_ONE, 4.0)

        testClock.instant = testClock
            .instant
            .plus(10, ChronoUnit.HOURS)

        checkEvent(EVENT_ONE, 0.0)

        for (i in listOf(10, 40)) {
            testClock.instant = testClock
                .instant
                .plus(10, ChronoUnit.MINUTES)

            repeat(HOUR_MINUTES) {  eventsStatistics.incEvent(EVENT_ONE) }
        }

        checkEvent(EVENT_ONE, 2.0)
    }

    private fun checkEvent(eventName: String, stat: Double) {
        val statEventOne = eventsStatistics.getEventStatisticByName(eventName)!!

        assertEquals(stat, statEventOne, EPS)

        val allStatistics = eventsStatistics.getAllEventStatistic()

        assertEquals(stat, allStatistics[eventName]!!, EPS)
    }

    private class TestClock(var instant: Instant) : Clock() {
        override fun instant(): Instant = instant

        override fun withZone(zone: ZoneId?): Clock = TODO()

        override fun getZone(): ZoneId = TODO()
    }
}