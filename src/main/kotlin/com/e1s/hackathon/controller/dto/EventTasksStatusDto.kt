package com.e1s.hackathon.controller.dto

import com.e1s.hackathon.model.TaskStatus
import java.time.Instant

/** Uczestnik zadania z danymi pracownika zamiast samego employeeId */
data class ParticipantTaskDto(
    val id: String?,
    val createdAt: Instant,
    val deadline: Instant?,
    val status: TaskStatus,
    val event: EventDto,
    val actionUrl: String?,
    val employeeFirstName: String,
    val employeeLastName: String
)

/** Struktura zgrupowanych zadań dla eventu */
data class EventTasksStatusDto(
    val pending: List<ParticipantTaskDto>,
    val done: List<ParticipantTaskDto>,
    val rejected: List<ParticipantTaskDto>
)