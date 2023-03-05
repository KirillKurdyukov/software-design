package ru.itmo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.model.FromTo
import ru.itmo.model.Statistic
import ru.itmo.service.StatisticService

@RestController
class StatisticsController(
    private val statisticService: StatisticService,
) {

    @PostMapping("/v1/statistic/visits/{id}")
    fun onStatisticVisits(
        @PathVariable("id", required = false) id: Int?,
    ): ResponseEntity<Statistic> = ResponseEntity.ok(
        statisticService.getStatistic(id),
    )

    @PostMapping("/v1/count/visits")
    fun onCountVisits(
        @RequestBody
        fromTo: FromTo,
    ): ResponseEntity<Int> = ResponseEntity.ok(
        statisticService.getCountVisit(fromTo),
    )
}
