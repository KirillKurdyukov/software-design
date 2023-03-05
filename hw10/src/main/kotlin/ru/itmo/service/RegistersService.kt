package ru.itmo.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.itmo.model.Event
import ru.itmo.model.Type
import ru.itmo.model.User
import ru.itmo.repository.EventRepository
import ru.itmo.repository.SeasonTicketRepository

@Service
class RegistersService(
    private val objectMapper: ObjectMapper,
    private val seasonTicketRepository: SeasonTicketRepository,
    private val eventRepository: EventRepository,
) {

    /**
     * @return user id in system
     */
    fun registerUser(user: User): Int {
        val userSerialize = objectMapper.writeValueAsString(user)

        val (seasonTicketId, version) = seasonTicketRepository
            .registerSeasonTicket(user.name)

        eventRepository.appendEvent(
            Event(
                seasonTicketId,
                version,
                Type.CREATE,
                userSerialize,
            ),
        )

        return seasonTicketId
    }
}
