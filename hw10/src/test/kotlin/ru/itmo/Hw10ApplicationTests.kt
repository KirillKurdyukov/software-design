package ru.itmo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.itmo.model.Action
import ru.itmo.model.AddedVisit
import ru.itmo.model.FromTo
import ru.itmo.model.Statistic
import ru.itmo.model.Ticket
import ru.itmo.model.Type
import ru.itmo.model.User
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.sql.DataSource
import kotlin.properties.Delegates

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(classes = [Hw10Application::class])
@ContextConfiguration(initializers = [Hw10ApplicationTests.Companion.Initializer::class])
@AutoConfigureMockMvc
class Hw10ApplicationTests {

    companion object {
        @Container
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13")
            .withPassword("123qweqwe")
            .withUsername("kurdyukov-kir")
            .withInitScript("migration/schema.sql")

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.jdbcUrl,
                    "spring.datasource.username=" + postgreSQLContainer.username,
                    "spring.datasource.password=" + postgreSQLContainer.password,
                ).applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var dataSource: DataSource

    private var userId by Delegates.notNull<Int>()
    private lateinit var userName: String

    @BeforeEach
    fun before() {
        userName = UUID.randomUUID().toString()
        userId = registerUser(userName)
    }

    @AfterEach
    fun after() {
        dataSource.connection
            .prepareStatement("delete from events where true;")
            .execute()
    }

    @Test
    fun `Should extend ticket on 2 days`() {
        action(
            Type.EXTEND,
            AddedVisit(count = 2),
        )

        checkTicket(2)
    }

    @Test
    fun `Should cancel ticket then ignore extend ticket`() {
        action(
            Type.CANCEL,
            Unit,
        )

        action(
            Type.EXTEND,
            AddedVisit(count = 2),
        )

        checkTicket(0)
    }

    @Test
    fun `Should skip in finiteness`() {
        action(
            Type.EXTEND,
            AddedVisit(count = 2),
        )

        assertEquals(true, inFitness())
        outFitness()

        checkTicket(1)
    }

    @Test
    fun `Should return correct statistic`() {
        action(
            Type.EXTEND,
            AddedVisit(count = 2),
        )

        inFitness()
        outFitness()

        inFitness()
        outFitness()

        val s = getStatistic()

        assertEquals(2, s.countTimes)
    }

    @Test
    fun `Should return correct count of visits in interval`() {
        action(
            Type.EXTEND,
            AddedVisit(count = 3),
        )

        inFitness()
        outFitness()
        inFitness()
        outFitness()
        inFitness()
        outFitness()

        val count = getCountInterval(
            FromTo(
                from = Instant.now()
                    .minus(3, ChronoUnit.HOURS),
                to = Instant.now()
                    .plus(1, ChronoUnit.HOURS),
            ),
        )

        assertEquals(3, count)

        val countZero = getCountInterval(
            FromTo(
                from = Instant.now()
                    .minus(3, ChronoUnit.HOURS),
                to = Instant.now()
                    .minus(1, ChronoUnit.HOURS),
            ),
        )

        assertEquals(0, countZero)
    }

    private fun checkTicket(expectedVisit: Int) {
        val ticket = getTicket()
        assertEquals(userName, ticket.userName)
        assertEquals(expectedVisit, ticket.count)
    }

    private fun registerUser(userName: String) = mockMvc
        .post("/v1/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                User(userName),
            )
        }
        .andExpect {
            status { isOk() }
        }
        .andReturn()
        .response
        .contentAsString
        .toInt()

    private fun action(type: Type, data: Any) {
        mockMvc.post("/v1/action") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                Action(
                    userId,
                    type,
                    objectMapper.writeValueAsString(data),
                ),
            )
        }.andExpect { status { isOk() } }
    }

    private fun getTicket(): Ticket = objectMapper.readValue(
        mockMvc
            .post("/v1/get/session-ticket") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(userId)
            }
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString,
    )

    private fun inFitness(): Boolean = mockMvc
        .post("/v1/in") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userId)
        }
        .andExpect { status { isOk() } }
        .andReturn()
        .response.contentAsString.toBoolean()

    private fun outFitness() {
        action(
            Type.END_VISIT,
            Unit,
        )
    }

    private fun getStatistic() = objectMapper.readValue<Statistic>(
        mockMvc
            .post("/v1/statistic/visits/$userId")
            .andExpect { status { isOk() } }
            .andReturn()
            .response.contentAsString,
    )

    private fun getCountInterval(fromTo: FromTo) = objectMapper.readValue<Int>(
        mockMvc
            .post("/v1/count/visits") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(fromTo)
            }
            .andExpect { status { isOk() } }
            .andReturn()
            .response.contentAsString,
    )
}
