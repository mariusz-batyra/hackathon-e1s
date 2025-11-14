package com.e1s.hackathon.service

import com.e1s.hackathon.model.TaskDocument
import com.e1s.hackathon.model.TaskStatus
import com.e1s.hackathon.repository.TaskRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TaskService(private val taskRepository: TaskRepository) {

    fun getTasksForUser(userId: String): List<TaskDocument> =
        taskRepository.findByEmployeeId(userId)

    fun markDone(taskId: String) = updateStatus(taskId, TaskStatus.DONE)

    fun reject(taskId: String) = updateStatus(taskId, TaskStatus.REJECTED)

    private fun updateStatus(taskId: String, status: TaskStatus) {
        val task = taskRepository.findById(taskId).orElseThrow { IllegalArgumentException("Task not found: $taskId") }
        val updated = if (status == TaskStatus.DONE) task.copy(status = status, doneAt = Instant.now()) else task.copy(status = status)
        taskRepository.save(updated)
    }
}
