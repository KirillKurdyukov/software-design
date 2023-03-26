package ru.itmo.userclient.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import ru.itmo.userclient.model.StocksSnapshot

@Component
class Client(
    private val objectMapper: ObjectMapper,
) {
    private val restTemplate = RestTemplate()
    private val headers = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
    }

    @Value("\${client.url}")
    private lateinit var url: String

    fun getStock(companyName: String) = restTemplate.exchange(
        "$url/get/stocks",
        HttpMethod.POST,
        HttpEntity(companyName, headers),
        StocksSnapshot::class.java,
    ).body!!

    fun sellStocks(companyName: String, count: Int) = restTemplate.exchange(
        "$url/sell/stocks",
        HttpMethod.POST,
        HttpEntity(objectMapper.writeValueAsString(
            mapOf(
                "companyName" to companyName,
                "count" to count,
            )
        ), headers),
        Int::class.java,
    ).body!!

    fun buyStock(companyName: String, count: Int, userMoney: Int): StocksSnapshot = restTemplate
        .exchange(
            "$url/buy/stocks",
            HttpMethod.POST,
            HttpEntity(
                objectMapper.writeValueAsString(
                    mapOf(
                        "companyName" to companyName,
                        "count" to count,
                        "userAmountMoney" to userMoney,
                    ),
                ),
                headers,
            ),
            StocksSnapshot::class.java,
        ).body!!
}
