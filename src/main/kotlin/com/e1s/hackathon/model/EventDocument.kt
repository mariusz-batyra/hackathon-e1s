package com.e1s.hackathon.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "events")
data class EventDocument(
    @Id
    val id: String? = null,
    val category: Category,
    val title: String,
    val description: String? = null,
    val groups: List<GroupEnum> = emptyList()
)

