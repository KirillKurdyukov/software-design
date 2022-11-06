package ru.itmo.hw4.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.itmo.hw4.model.ListWorks
import ru.itmo.hw4.model.Work
import java.util.Optional
import javax.sql.DataSource

@Repository
class WorkRepository(
    dataSource: DataSource
) : JdbcTemplate(dataSource) {

    private val listMapper = RowMapper { rs, _ ->
        ListWorks(
            rs.getInt("ListId"),
            rs.getString("ListName"),
        )
    }

    private val workMapper = RowMapper { rs, _ ->
        Work(
            rs.getInt("WorkId"),
            rs.getString("WorkName"),
            rs.getBoolean("IsReady"),
        )
    }

    fun findAllListsWorks(): List<ListWorks> = query(
        "select ListId, ListName from ListsWorks;",
        listMapper,
    )

    fun findWorksByListId(id: Int): List<Work> = query(
        """
            select WorkId, WorkName, IsReady
            from Works
                     natural join ListsWorks
            where ListId = ?;
        """.trimIndent(),
        workMapper,
        id,
    )

    fun findListById(id: Int): Optional<ListWorks> = query(
        "select ListId, ListName from ListsWorks where ListId = ?;",
        listMapper,
        id,
    ).stream().findFirst()

    fun saveListWorks(listName: String) {
        update("""insert into ListsWorks(ListName) values (?)""", listName)
    }

    fun deleteListWorks(listId: Int) {
        update("""delete from ListsWorks where ListId = ?""", listId)
    }

    fun saveWork(listId: Int, workName: String) {
        update("""insert into Works(ListId, WorkName) values (?, ?)""", listId, workName)
    }

    fun deleteWork(workId: Int) {
        update("""delete from Works where WorkId = ?;""", workId)
    }

    fun markDoneWork(workId: Int) {
        update("""update Works set isReady = true where WorkId = ?""", workId)
    }
}