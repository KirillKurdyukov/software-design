package ru.itmo.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class SeasonTicketRepository(
    dataSource: DataSource,
) : JdbcTemplate(dataSource) {

    fun registerSeasonTicket(nameUser: String): Pair<Int, Int> = query(
        "insert into season_tickets(name) values (?) returning id, version;",
        { rs, _ -> rs.getInt("id") to rs.getInt("version") },
        nameUser,
    ).single()

    fun getNextVersionBySeasonTicketId(seasonTicketId: Int): Int = query(
        "update season_tickets set version = version + 1 where id = ? returning version;",
        { rs, _ -> rs.getInt("version") },
        seasonTicketId,
    ).single()

    fun findAll(): List<Int> = query(
        "select id from season_tickets;",
    ) { rs, _ -> rs.getInt("id") }
}
