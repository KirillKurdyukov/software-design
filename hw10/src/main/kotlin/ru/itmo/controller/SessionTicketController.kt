package ru.itmo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.model.Action
import ru.itmo.model.Ticket
import ru.itmo.model.Type
import ru.itmo.model.User
import ru.itmo.service.EventService
import ru.itmo.service.RegistersService

@RestController
class SessionTicketController(
    private val registersService: RegistersService,
    private val eventService: EventService,
) {

    @PostMapping("/v1/register")
    fun onRegister(
        @RequestBody user: User,
    ): ResponseEntity<Int> = ResponseEntity.ok(registersService.registerUser(user))

    @PostMapping("/v1/get/session-ticket")
    fun onGetCountVisits(
        @RequestBody id: Int,
    ): ResponseEntity<Ticket> = ResponseEntity.ok(
        eventService.getSessionTicketById(id),
    )

    @PostMapping("/v1/in")
    fun onIn(
        @RequestBody id: Int,
    ): ResponseEntity<Boolean> {
        val ticket = eventService.getSessionTicketById(id)

        return if (ticket.count > 0) {
            eventService.appendAction(
                Action(
                    id,
                    Type.START_VISIT,
                    "{}",
                ),
            )

            ResponseEntity.ok(true)
        } else {
            ResponseEntity.ok(false)
        }
    }
}
