package ru.itmo.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import ru.itmo.exceptions.NotFoundExceptionBySessionTicketId
import ru.itmo.model.Action
import ru.itmo.model.AddedVisit
import ru.itmo.model.Event
import ru.itmo.model.Ticket
import ru.itmo.model.Type
import ru.itmo.repository.EventRepository
import ru.itmo.repository.SeasonTicketRepository

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val seasonTicketRepository: SeasonTicketRepository,
    private val objectMapper: ObjectMapper,
) {

    fun appendAction(action: Action) {
        val currentVersion = seasonTicketRepository
            .getNextVersionBySeasonTicketId(action.sessionTicketId)

        eventRepository.appendEvent(
            Event(
                action.sessionTicketId,
                currentVersion,
                action.type,
                action.data,
            ),
        )
    }

    fun getSessionTicketById(id: Int): Ticket {
        val sortedEvents = eventRepository.eventBySeasonTicketId(id)
        if (sortedEvents.isEmpty()) {
            throw NotFoundExceptionBySessionTicketId(id)
        }

        assert(sortedEvents[0].eventType == Type.CREATE)
        var res = 0
        val name = sortedEvents[0].name

        sortedEvents.forEach {
            when (it.eventType) {
                Type.EXTEND ->
                    res += objectMapper
                        .readValue<AddedVisit>(it.data).count

                Type.START_VISIT -> res--
                Type.CANCEL -> return Ticket(name, 0)
                else -> {}
            }
        }

        return Ticket(name, res)
    }
}
