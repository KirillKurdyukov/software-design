package ru.itmo.nasa

import kotlinx.coroutines.runBlocking
import ru.itmo.nasa.api.NasaApi

fun main() {
    println(
        runBlocking {
            NasaApi("https://api.nasa.gov").getSolarFlaresInPeriod(
                "2022-09-01",
                "2022-09-20",
            )
        }
    )
}