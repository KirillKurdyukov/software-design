package ru.itmo.exceptions

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [NotFoundException::class])
    fun onNotFoundException(e: NotFoundException): ResponseEntity<Unit> {
        return ResponseEntity.notFound().build()
    }
}
