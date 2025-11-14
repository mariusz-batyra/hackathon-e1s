package com.e1s.hackathon.controller

import com.e1s.hackathon.controller.dto.CategoryDto
import com.e1s.hackathon.controller.dto.NotificationChannelDto
import com.e1s.hackathon.controller.dto.toDto
import com.e1s.hackathon.controller.dto.toModel
import com.e1s.hackathon.service.UserSettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/me")
class UserSettingsController(private val userSettingsService: UserSettingsService) {

    @GetMapping("/categories/blacklist")
    fun getBlacklist(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<CategoryDto>> {
        val categories = userSettingsService.getBlacklist(userId)
        return ResponseEntity.ok(categories.map { it.toDto() })
    }

    @PutMapping("/categories/blacklist")
    fun replaceBlacklist(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody categories: List<CategoryDto>
    ): ResponseEntity<List<CategoryDto>> {
        val updatedCategories = userSettingsService.replaceBlacklist(
            userId,
            categories.map { it.toModel() }
        )
        return ResponseEntity.ok(updatedCategories.map { it.toDto() })
    }

    @GetMapping("/notification-channels")
    fun getNotificationChannels(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<NotificationChannelDto>> {
        val channels = userSettingsService.getNotificationChannels(userId)
        return ResponseEntity.ok(channels.map { it.toDto() })
    }

    @PutMapping("/notification-channels")
    fun replaceNotificationChannels(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody channels: List<NotificationChannelDto>
    ): ResponseEntity<List<NotificationChannelDto>> {
        val updatedChannels = userSettingsService.replaceNotificationChannels(
            userId,
            channels.map { it.toModel() }
        )
        return ResponseEntity.ok(updatedChannels.map { it.toDto() })
    }
}

