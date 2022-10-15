package ru.itmo.nasa.api

import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.model.Parameter

object NasaServerTest {
    private lateinit var mockNasaServer: MockServerClient

    fun start(port: Int) {
        println("Starting mock nasa server...")

        mockNasaServer = ClientAndServer.startClientAndServer(port)
    }

    fun stop() {
        println("Stopping mocked nase server...")

        mockNasaServer.stop()
    }

    fun mockMethodNasaServer(
        body: String,
        vararg additionalParams: Parameter
    ) {
        mockNasaServer
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/DONKI/FLR")
                    .withQueryStringParameters(*additionalParams)
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
                    .withBody(body)
            )
    }
}