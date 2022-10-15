package ru.itmo.nasa.model

import java.net.URI

data class SolarFlare(
    val flrID: String,
    val instruments: List<Instrument>,
    val beginTime: String,
    val peakTime: String,
    val endTime: String,
    val classType: String,
    val sourceLocation: String,
    val activeRegionNum: Long,
    val linkedEvents: Any?,
    val link: URI,
)

data class Instrument(
    val displayName: String,
)