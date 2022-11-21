package ru.itmo

import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.LinkedList

/**
 * Single thread implementation
 */
class EventsStatisticsImpl(
    private val clock: Clock
) : EventsStatistics {
    private companion object {
        const val HOUR_MINUTES = 60
    }

    private val queueEvents = LinkedList<Event>()
    private val statistics = mutableMapOf<String, Int>()

    override fun incEvent(eventName: String) {
        statistics.merge(eventName, 1, Int::plus)
        queueEvents.add(Event(clock.instant(), eventName))
    }

    override fun getEventStatisticByName(eventName: String): Double? {
        clearQueue()

        return statistics[eventName]?.toDouble()?.div(HOUR_MINUTES)
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        clearQueue()

        return statistics.mapValues { it.value.toDouble() / HOUR_MINUTES }
    }

    override fun printStatistic() {
        val allEventsStatistic = getAllEventStatistic()

        allEventsStatistic.forEach {
            println("Event: ${it.key}, RMS: ${it.value}")
        }
    }

    private fun clearQueue() {
        val hourAgo = clock.instant().minus(1, ChronoUnit.HOURS)

        while (queueEvents.isNotEmpty() && queueEvents.peek().time < hourAgo) {
            val popEvent = queueEvents.pop()

            statistics.compute(popEvent.name) { _, oldCount ->
                oldCount!!.minus(1)
            }
        }
    }

    private data class Event(
        val time: Instant,
        val name: String,
    )
}