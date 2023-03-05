package ru.itmo.exceptions

abstract class NotFoundException(message: String) : Exception(message)

class NotFoundExceptionBySessionTicketId(id: Int) : NotFoundException("Not found sessionTicketId: $id")