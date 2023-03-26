package ru.itmo.userclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.FixedHostPortGenericContainer
import ru.itmo.userclient.model.AddMoneyRequest
import ru.itmo.userclient.model.BuyStockRequest
import ru.itmo.userclient.model.Stocks
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class UserClientApplicationTests {

    class MyContainer(dockerImage: String) : FixedHostPortGenericContainer<MyContainer>(dockerImage)

    private val container =
        MyContainer("ru.itmo/stock-market:0.0.1-snapshot")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080)

    private val restTemplate = RestTemplate()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val headers = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
    }

    private lateinit var userId: String

    @BeforeEach
    fun before() {
        container.start()

        restTemplate.exchange(
            "http://localhost:8080/add/company",
            HttpMethod.POST,
            HttpEntity("Yandex", headers),
            Unit::class.java,
        )

        restTemplate.exchange(
            "http://localhost:8080/add/stocks",
            HttpMethod.POST,
            HttpEntity(
                objectMapper.writeValueAsString(
                    mapOf(
                        "companyName" to "Yandex",
                        "count" to 200,
                    ),
                ),
                headers,
            ),
            Unit::class.java,
        )

        restTemplate.exchange(
            "http://localhost:8080/set/cost",
            HttpMethod.POST,
            HttpEntity(
                objectMapper.writeValueAsString(
                    mapOf(
                        "companyName" to "Yandex",
                        "cost" to 100,
                    ),
                ),
                headers,
            ),
            Unit::class.java,
        )

        userId = mockMvc.post("/register") {
            contentType = MediaType.APPLICATION_JSON
            content = "Kirill Kurdyukov"
        }.andExpect {
            status { isOk() }
        }.andReturn().response.contentAsString

        mockMvc.post("/add/money") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                AddMoneyRequest(
                    id = userId,
                    money = 1000,
                ),
            )
        }.andExpect {
            status { isOk() }
        }
    }

    @AfterEach
    fun after() {
        container.stop()
    }

    @Test
    fun `Not found user`() {
        mockMvc.get("/get/user/money") {
            contentType = MediaType.APPLICATION_JSON
            content = UUID.randomUUID().toString()
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `User buys stocks then stocks up`() {
        mockMvc.post("/buy/stocks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BuyStockRequest(
                    id = userId,
                    companyName = "Yandex",
                    count = 5,
                ),
            )
        }.andExpect {
            status { isOk() }
        }

        restTemplate.exchange(
            "http://localhost:8080/set/cost",
            HttpMethod.POST,
            HttpEntity(
                objectMapper.writeValueAsString(
                    mapOf(
                        "companyName" to "Yandex",
                        "cost" to 300,
                    ),
                ),
                headers,
            ),
            Unit::class.java,
        )

        assertEquals(
            2000,
            mockMvc.get("/get/user/money") {
                contentType = MediaType.APPLICATION_JSON
                content = userId
            }.andExpect {
                status { isOk() }
            }.andReturn()
                .response
                .contentAsString
                .toInt(),
        )

        assertEquals(
            objectMapper.writeValueAsString(
                listOf(Stocks(5, "Yandex", 300)),
            ),
            mockMvc.get("/get/stocks") {
                contentType = MediaType.APPLICATION_JSON
                content = userId
            }.andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString,
        )
    }

    @Test
    fun `User buys stocks then sell stocks`() {
        mockMvc.post("/buy/stocks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BuyStockRequest(
                    id = userId,
                    companyName = "Yandex",
                    count = 5,
                ),
            )
        }.andExpect {
            status { isOk() }
        }

        assertEquals(
            1000,
            mockMvc.get("/get/user/money") {
                contentType = MediaType.APPLICATION_JSON
                content = userId
            }.andExpect {
                status { isOk() }
            }.andReturn()
                .response
                .contentAsString
                .toInt(),
        )

        assertEquals(
            objectMapper.writeValueAsString(
                listOf(Stocks(5, "Yandex", 100)),
            ),
            mockMvc.get("/get/stocks") {
                contentType = MediaType.APPLICATION_JSON
                content = userId
            }.andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString,
        )

        mockMvc.post("/sell/stocks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                BuyStockRequest(
                    id = userId,
                    companyName = "Yandex",
                    count = 5,
                ),
            )
        }.andExpect {
            status { isOk() }
        }

        assertEquals(
            objectMapper.writeValueAsString(
                listOf<Stocks>(),
            ),
            mockMvc.get("/get/stocks") {
                contentType = MediaType.APPLICATION_JSON
                content = userId
            }.andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString,
        )
    }
}
