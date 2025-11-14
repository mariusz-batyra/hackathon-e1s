package com.e1s.hackathon.controller

import com.e1s.hackathon.model.Category
import com.e1s.hackathon.model.NotificationChannel
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
    fun getBlacklist(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<Category>> =
        ResponseEntity.ok(userSettingsService.getBlacklist(userId))

    @PutMapping("/categories/blacklist")
    fun replaceBlacklist(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody categories: List<Category>
    ): ResponseEntity<List<Category>> =
        ResponseEntity.ok(userSettingsService.replaceBlacklist(userId, categories))

    @GetMapping("/notification-channels")
    fun getNotificationChannels(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<NotificationChannel>> =
        ResponseEntity.ok(userSettingsService.getNotificationChannels(userId))

    @PutMapping("/notification-channels")
    fun replaceNotificationChannels(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody channels: List<NotificationChannel>
    ): ResponseEntity<List<NotificationChannel>> =
        ResponseEntity.ok(userSettingsService.replaceNotificationChannels(userId, channels))
}

