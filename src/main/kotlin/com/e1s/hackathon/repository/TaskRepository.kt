package com.e1s.hackathon.repository

import com.e1s.hackathon.model.TaskDocument
import com.e1s.hackathon.model.TaskStatus
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : MongoRepository<TaskDocument, String> {
    fun findByEmployeeId(employeeId: String): List<TaskDocument>
    fun findByEmployeeIdAndStatus(employeeId: String, status: TaskStatus): List<TaskDocument>
}
