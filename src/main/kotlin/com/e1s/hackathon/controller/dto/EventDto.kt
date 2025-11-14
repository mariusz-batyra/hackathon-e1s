package com.e1s.hackathon.controller.dto

import com.e1s.hackathon.model.GroupEnum

/**
 * DTO for Event response.
 */
data class EventDto(
    val id: String?,
    val category: CategoryDto,
    val title: String,
    val description: String?,
    val groups: List<GroupEnum>
)

