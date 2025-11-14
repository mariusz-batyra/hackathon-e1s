package com.e1s.hackathon.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "tasks")
data class TaskDocument(
    @Id
    val id: String? = null,
    val createdAt: Instant = Instant.now(),
    val deadline: Instant? = null,
    val event: EventDocument,
    val status: TaskStatus = TaskStatus.NEW,
    val actionUrl: String? = null,
    val employeeId: String
)

