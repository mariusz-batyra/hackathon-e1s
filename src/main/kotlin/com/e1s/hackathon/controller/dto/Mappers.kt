package com.e1s.hackathon.controller.dto

import com.e1s.hackathon.model.*

/**
 * Mapper functions to convert between DTOs and Domain/Document classes.
 */

// Category mappings
fun CategoryDto.toModel(): Category = Category.valueOf(this.name)

fun Category.toDto(): CategoryDto = CategoryDto.valueOf(this.name)

// NotificationPolicy mappings
fun NotificationPolicyDto.toModel(): NotificationPolicy = NotificationPolicy.valueOf(this.name)

fun NotificationPolicy.toDto(): NotificationPolicyDto = NotificationPolicyDto.valueOf(this.name)

// NotificationChannel mappings
fun NotificationChannelDto.toModel(): NotificationChannel = NotificationChannel.valueOf(this.name)

fun NotificationChannel.toDto(): NotificationChannelDto = NotificationChannelDto.valueOf(this.name)

// Event mappings
fun EventDto.toDocument(): EventDocument = EventDocument(
    id = this.id,
    category = this.category.toModel(),
    title = this.title,
    description = this.description,
    groups = this.groups
)

fun EventDocument.toDto(): EventDto = EventDto(
    id = this.id,
    category = this.category.toDto(),
    title = this.title,
    description = this.description,
    groups = this.groups
)

// Task mappings
fun TaskDto.toDocument(): TaskDocument = TaskDocument(
    id = this.id,
    createdAt = this.createdAt,
    deadline = this.deadline,
    event = this.event.toDocument(),
    status = this.status,
    actionUrl = this.actionUrl,
    employeeId = this.employeeId
)

fun TaskDocument.toDto(): TaskDto = TaskDto(
    id = this.id,
    createdAt = this.createdAt,
    deadline = this.deadline,
    event = this.event.toDto(),
    status = this.status,
    actionUrl = this.actionUrl,
    employeeId = this.employeeId
)

