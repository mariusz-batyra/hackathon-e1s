package com.e1s.hackathon.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "employees")
data class EmployeeDocument(
    @Id
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val categoriesBlacklist: List<Category> = emptyList(),
    val groups: List<GroupEnum> = emptyList(),
    val position: String? = null,
    val notificationChannels: List<NotificationChannel> = emptyList()
)

