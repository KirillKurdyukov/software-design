package ru.itmo.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Action(
    @field:JsonProperty("session_ticket_id", required = true)
    val sessionTicketId: Int,
    val type: Type,
    val data: String,
)

enum class Type {
    CANCEL,
    EXTEND,
    CREATE,
    START_VISIT,
    END_VISIT,
}
