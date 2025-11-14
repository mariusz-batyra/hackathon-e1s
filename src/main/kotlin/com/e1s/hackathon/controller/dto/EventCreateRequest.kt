package com.e1s.hackathon.controller.dto

import com.e1s.hackathon.model.GroupEnum

/**
 * Request body for creating an event.
 */
data class EventCreateRequest(
    val category: CategoryDto,
    val title: String,
    val description: String? = null,
    val groups: List<GroupEnum> = emptyList()
)

