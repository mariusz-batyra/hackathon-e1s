package com.e1s.hackathon.controller

import com.e1s.hackathon.model.TaskDocument
import com.e1s.hackathon.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getTasksForCurrentUser(@RequestHeader("X-User-Id") userId: String): ResponseEntity<List<TaskDocument>> {
        val tasks = taskService.getTasksForUser(userId)
        return ResponseEntity.ok(tasks)
    }

    @PostMapping("/{taskId}/done")
    fun markDone(@PathVariable taskId: String): ResponseEntity<Void> {
        taskService.markDone(taskId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{taskId}/reject")
    fun reject(@PathVariable taskId: String): ResponseEntity<Void> {
        taskService.reject(taskId)
        return ResponseEntity.ok().build()
    }
}

