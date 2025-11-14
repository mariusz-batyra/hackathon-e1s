package com.e1s.hackathon.controller

import com.e1s.hackathon.controller.dto.EventCreateRequest
import com.e1s.hackathon.controller.dto.EventDto
import com.e1s.hackathon.controller.dto.toDto
import com.e1s.hackathon.controller.dto.toModel
import com.e1s.hackathon.model.EventDocument
import com.e1s.hackathon.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventDto>> {
        val events = eventService.getAllEvents()
        return ResponseEntity.ok(events.map { it.toDto() })
    }

    @PostMapping
    fun createEvent(@RequestBody request: EventCreateRequest): ResponseEntity<EventDto> {
        val eventDocument = EventDocument(
            category = request.category.toModel(),
            title = request.title,
            description = request.description,
            groups = request.groups
        )
        val saved = eventService.createEvent(eventDocument)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.toDto())
    }
}

