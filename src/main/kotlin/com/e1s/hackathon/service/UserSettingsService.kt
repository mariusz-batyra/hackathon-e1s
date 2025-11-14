package com.e1s.hackathon.service

import com.e1s.hackathon.model.Category
import com.e1s.hackathon.model.EmployeeDocument
import com.e1s.hackathon.model.NotificationChannel
import com.e1s.hackathon.repository.EmployeeRepository
import org.springframework.stereotype.Service

@Service
class UserSettingsService(private val employeeRepository: EmployeeRepository) {

    fun getUser(userId: String): EmployeeDocument =
        employeeRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found: $userId") }

    fun getBlacklist(userId: String): List<Category> = getUser(userId).categoriesBlacklist

    fun replaceBlacklist(userId: String, categories: List<Category>): List<Category> {
        val user = getUser(userId)
        val saved = employeeRepository.save(user.copy(categoriesBlacklist = categories))
        return saved.categoriesBlacklist
    }

    fun getNotificationChannels(userId: String): List<NotificationChannel> = getUser(userId).notificationChannels

    fun replaceNotificationChannels(userId: String, channels: List<NotificationChannel>): List<NotificationChannel> {
        val user = getUser(userId)
        val saved = employeeRepository.save(user.copy(notificationChannels = channels))
        return saved.notificationChannels
    }
}

