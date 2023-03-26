package ru.itmo.stockmarket.exceptions

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class NotFoundException : Exception()

class ConflictException : Exception()

@ControllerAdvice
class StockMarketExceptionHandler {

    @ExceptionHandler(value = [NotFoundException::class])
    fun onFoundException(ex: NotFoundException): ResponseEntity<Unit> =
        ResponseEntity.notFound().build()

    @ExceptionHandler(value = [ConflictException::class])
    fun onConflictException(ex: ConflictException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatusCode.valueOf(409)).build()
}
