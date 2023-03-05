package ru.itmo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.model.Action
import ru.itmo.service.EventService

@RestController
class ActionController(
    private val eventService: EventService,
) {

    @PostMapping("/v1/action")
    fun onExtend(
        @RequestBody action: Action,
    ): ResponseEntity<Unit> {
        eventService.appendAction(action)

        return ResponseEntity.ok().build()
    }
}
