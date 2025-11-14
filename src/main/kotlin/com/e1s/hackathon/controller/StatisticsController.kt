package com.e1s.hackathon.controller

import com.e1s.hackathon.service.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stats")
class StatisticsController(private val statisticsService: StatisticsService) {

    @GetMapping
    fun getStats(@RequestParam(name = "expiringHours", required = false) expiringHours: Long?): StatisticsService.StatsResponse {
        return statisticsService.getStats(expiringHours ?: 24)
    }
}

