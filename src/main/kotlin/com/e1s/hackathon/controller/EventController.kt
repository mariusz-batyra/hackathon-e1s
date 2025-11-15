package com.e1s.hackathon.controller

import com.e1s.hackathon.controller.dto.EventCreateRequest
import com.e1s.hackathon.controller.dto.EventDto
import com.e1s.hackathon.controller.dto.EventTasksStatusDto
import com.e1s.hackathon.controller.dto.ParticipantTaskDto
import com.e1s.hackathon.controller.dto.toDto
import com.e1s.hackathon.controller.dto.toModel
import com.e1s.hackathon.model.EventDocument
import com.e1s.hackathon.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/{eventId}/participants-status")
    fun getParticipantsStatus(@PathVariable eventId: String): ResponseEntity<EventTasksStatusDto> {
        val allTasks = eventService.getEventTasks(eventId)
        val employeesById = eventService.getEmployeesMap() // helper we will add in service
        val participants = allTasks.map { task ->
            val emp = employeesById[task.employeeId]
            ParticipantTaskDto(
                id = task.id,
                createdAt = task.createdAt,
                deadline = task.deadline,
                status = task.status,
                event = task.event.toDto(),
                actionUrl = task.actionUrl,
                employeeFirstName = emp?.firstName ?: "UNKNOWN",
                employeeLastName = emp?.lastName ?: "UNKNOWN"
            )
        }
        val dto = EventTasksStatusDto(
            pending = participants.filter { it.status == com.e1s.hackathon.model.TaskStatus.NEW },
            done = participants.filter { it.status == com.e1s.hackathon.model.TaskStatus.DONE },
            rejected = participants.filter { it.status == com.e1s.hackathon.model.TaskStatus.REJECTED }
        )
        return ResponseEntity.ok(dto)
    }
}
