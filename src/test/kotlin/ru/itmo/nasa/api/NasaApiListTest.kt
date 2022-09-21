package ru.itmo.nasa.api

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockserver.model.Parameter

class NasaApiListTest : BaseTest() {
    @Test
    fun `Check get interval at solar flares with specified classType`() {
        NasaServerTest.mockMethodNasaServer(
            listIntervalSolarFlaresBody,
            Parameter.param(NasaApi.API_KEY_NAME, NasaApi.API_KEY_VALUE),
            Parameter.param("startDate", "2022-09-01"),
            Parameter.param("endDate", "2022-09-18"),
        )

        val listSolarFlares = runBlocking {
            NasaApi("${host}:${port}")
                .getSolarFlaresInPeriod(
                    startDate = "2022-09-01",
                    endDate = "2022-09-18",
                    classType = "M1.0",
                )
        }

        Assertions.assertEquals(2, listSolarFlares.size)
    }
}