package ru.itmo.stockmarket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.itmo.stockmarket.model.SetCostRequest
import ru.itmo.stockmarket.model.StocksSnapshot

@SpringBootTest
@AutoConfigureMockMvc
class StockMarketApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `Add stocks then buy stocks then set cost ang get`() {
        mockMvc.post("/add/company") {
            contentType = MediaType.APPLICATION_JSON
            content = "Yandex"
        }.andExpect { status { isOk() } }

        mockMvc.post("/add/stocks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                mapOf(
                    "companyName" to "Yandex",
                    "count" to 200,

                ),
            )
        }.andExpect { status { isOk() } }

        mockMvc.post("/set/cost") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                mapOf(
                    "companyName" to "Yandex",
                    "cost" to 100,
                ),
            )
        }.andExpect { status { isOk() } }

        val stocksSnapshot = objectMapper.readValue<StocksSnapshot>(
            mockMvc.post("/buy/stocks") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(
                    mapOf(
                        "companyName" to "Yandex",
                        "count" to 5,
                        "userAmountMoney" to 1000,
                    ),
                )
            }.andExpect { status { isOk() } }
                .andReturn().response.contentAsString,
        )

        assertEquals(5, stocksSnapshot.count)
        assertEquals(100, stocksSnapshot.cost)

        mockMvc.post("/set/cost") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                SetCostRequest("Yandex", 300),
            )
        }.andExpect { status { isOk() } }

        val stocks = objectMapper.readValue<StocksSnapshot>(
            mockMvc.post("/get/stocks") {
                contentType = MediaType.APPLICATION_JSON
                content = "Yandex"
            }.andExpect { status { isOk() } }
                .andReturn().response.contentAsString,
        )

        assertEquals(300, stocks.cost)
        assertEquals(195, stocks.count)
    }
}
