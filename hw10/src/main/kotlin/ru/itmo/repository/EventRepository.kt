package ru.itmo.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.itmo.model.Event
import ru.itmo.model.FullEvent
import ru.itmo.model.Type
import java.sql.Timestamp
import java.time.Instant
import javax.sql.DataSource

@Repository
class EventRepository(
    dataSource: DataSource,
) : JdbcTemplate(dataSource) {

    private val eventMapper = RowMapper { rs, _ ->
        FullEvent(
            rs.getString("name"),
            rs.getInt("season_ticket_id"),
            rs.getInt("version"),
            rs.getTimestamp("created_at").toInstant(),
            Type.valueOf(rs.getString("event_type")),
            rs.getString("data"),
        )
    }

    fun appendEvent(event: Event) {
        update(
            "insert into events(season_ticket_id, version, event_type, data) values (?, ?, ?, ?);",
            event.seasonTicketId,
            event.version,
            event.eventType.name,
            event.data,
        )
    }

    fun eventBySeasonTicketId(id: Int): List<FullEvent> = query(
        """
            select name, season_ticket_id, e.version, e.created_at, e.event_type, e.data
                from season_tickets st
            inner join events e on st.id = e.season_ticket_id
            where season_ticket_id = ?
            order by e.version;
        """.trimIndent(),
        eventMapper,
        id,
    )

    fun countEvents(from: Instant, to: Instant): Int = query(
        """
            select count(*) from events 
            where (created_at >= ? or created_at <= ?) and event_type = 'START_VISIT';
        """.trimIndent(),
        { rs, _ -> rs.getInt("count") },
        Timestamp.from(from),
        Timestamp.from(to),
    ).single()
}
