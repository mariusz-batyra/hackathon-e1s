package com.e1s.hackathon.controller

import com.e1s.hackathon.controller.dto.EventCreateRequest
import com.e1s.hackathon.model.EventDocument
import com.e1s.hackathon.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @PostMapping
    fun createEvent(@RequestBody request: EventCreateRequest): ResponseEntity<EventDocument> {
        val saved = eventService.createEvent(
            EventDocument(
                category = request.category,
                title = request.title,
                description = request.description,
                groups = request.groups
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}

