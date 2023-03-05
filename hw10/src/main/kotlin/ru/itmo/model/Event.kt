package ru.itmo.model

import java.time.Instant

data class Event(
    val seasonTicketId: Int,
    val version: Int,
    val eventType: Type,
    val data: String,
)

data class FullEvent(
    val name: String,
    val seasonTicketId: Int,
    val version: Int,
    val createdAt: Instant,
    val eventType: Type,
    val data: String,
)
