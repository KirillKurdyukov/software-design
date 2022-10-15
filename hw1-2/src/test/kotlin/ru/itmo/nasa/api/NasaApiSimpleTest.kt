package ru.itmo.nasa.api

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockserver.model.Parameter

class NasaApiSimpleTest : BaseTest() {
    @Test
    fun `Check get solar flare for today`() {
        NasaServerTest.mockMethodNasaServer(
            solarFlareBody,
            Parameter.param(NasaApi.API_KEY_NAME, NasaApi.API_KEY_VALUE),
            Parameter.param("startDate", "2022-09-21")
        )

        val (today) = runBlocking {
            NasaApi("$host:$port")
                .getSolarFlaresInPeriod(
                    startDate = "2022-09-21"
                )
        }

        assertEquals("M1.0", today.classType)
    }
}
