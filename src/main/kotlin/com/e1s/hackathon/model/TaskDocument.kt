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
    val employeeId: String,
    // Liczba wysłanych powiadomień powiązanych z tym zadaniem (sumarycznie różnymi kanałami)
    val deliveredNotifications: Int = 0,
    // Liczba unikalnych kliknięć w link akcji (np. śledzone parametrem)
    val uniqueClicks: Int = 0,
    // Moment oznaczenia jako DONE (do wyliczenia średniego czasu realizacji)
    val doneAt: Instant? = null,
    // Snapshot wybranego kanału w momencie pierwszej wysyłki (upraszcza analizę skuteczności)
    val primaryChannel: NotificationChannel? = null
)
