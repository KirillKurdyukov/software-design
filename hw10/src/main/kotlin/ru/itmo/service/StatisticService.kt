package ru.itmo.service

import org.springframework.stereotype.Service
import ru.itmo.model.FromTo
import ru.itmo.model.Statistic
import ru.itmo.model.Type
import ru.itmo.repository.EventRepository
import ru.itmo.repository.SeasonTicketRepository
import java.time.Instant

@Service
class StatisticService(
    private val eventRepository: EventRepository,
    private val seasonTicketRepository: SeasonTicketRepository,
) {

    /**
     * If id == null return all statistic
     */
    fun getStatistic(id: Int?): Statistic {
        val sortedEvents = if (id == null) {
            seasonTicketRepository.findAll()
                .flatMap { eventRepository.eventBySeasonTicketId(it) }
        } else {
            eventRepository.eventBySeasonTicketId(id)
        }

        var countTimes = 0
        lateinit var currentStart: Instant
        val intervals = mutableListOf<Long>()

        sortedEvents.forEach {
            when (it.eventType) {
                Type.START_VISIT -> {
                    currentStart = it.createdAt

                    countTimes++
                }

                Type.END_VISIT -> {
                    intervals.add(
                        it.createdAt.toEpochMilli() - currentStart.toEpochMilli(),
                    )
                }

                else -> { /* skip */
                }
            }
        }

        if (countTimes != intervals.size) {
            intervals.add(Instant.now().toEpochMilli() - currentStart.toEpochMilli())
        }

        return Statistic(
            Instant.ofEpochMilli(intervals.sum() / countTimes),
            countTimes,
        )
    }

    fun getCountVisit(fromTo: FromTo): Int = eventRepository.countEvents(fromTo.from, fromTo.to)
}
