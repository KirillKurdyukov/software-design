package ru.itmo.userclient.exceptions

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class NotFoundException : Exception()

@ControllerAdvice
class StockClientExceptionHandler {

    @ExceptionHandler(value = [NotFoundException::class])
    fun onFoundException(ex: NotFoundException): ResponseEntity<Unit> =
        ResponseEntity.notFound().build()
}
