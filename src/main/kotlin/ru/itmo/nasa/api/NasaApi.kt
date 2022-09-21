package ru.itmo.nasa.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.takeFrom
import ru.itmo.nasa.model.SolarFlare

class NasaApi(
    private val hostApi: String,
) {
    companion object {
        const val API_KEY_VALUE = "ZTisgzsw5cxJGGUxCZ4oATKHtgKhZIRdxebtfzu3"
        const val API_KEY_NAME = "api_key"

        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    setSerializationInclusion(JsonInclude.Include.ALWAYS)
                }
            }
        }
    }

    suspend fun getSolarFlaresInPeriod(
        startDate: String? = null,
        endDate: String? = null,
        classType: String? = null,
    ) = client
        .request<List<SolarFlare>> {
            url {
                takeFrom("$hostApi/DONKI/FLR")

                parameter(API_KEY_NAME, API_KEY_VALUE)

                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }

            method = HttpMethod.Get
        }
        .filter {
            classType ?: return@filter true

            classType == it.classType
        }
}