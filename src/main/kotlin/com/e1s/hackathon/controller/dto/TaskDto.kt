package com.e1s.hackathon.controller.dto

import com.e1s.hackathon.model.TaskStatus
import java.time.Instant

/**
 * DTO for Task response.
 */
data class TaskDto(
    val id: String?,
    val createdAt: Instant,
    val deadline: Instant?,
    val event: EventDto,
    val status: TaskStatus,
    val actionUrl: String?,
    val employeeId: String
)


