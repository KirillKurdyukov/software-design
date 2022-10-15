package ru.itmo.nasa.api

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

open class BaseTest {
    companion object {
        const val host = "http://localhost"
        const val port = 12321

        @JvmStatic
        @BeforeAll
        fun startServer() {
            NasaServerTest.start(port)
        }

        @JvmStatic
        @AfterAll
        fun stopServer() {
            NasaServerTest.stop()
        }
    }
}