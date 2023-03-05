package ru.itmo.model

import java.time.Instant

data class Statistic(
    val averageTimeFitness: Instant,
    val countTimes: Int,
)
