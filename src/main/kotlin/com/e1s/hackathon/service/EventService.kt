package com.e1s.hackathon.service

import com.e1s.hackathon.model.EventDocument
import com.e1s.hackathon.model.TaskDocument
import com.e1s.hackathon.repository.EmployeeRepository
import com.e1s.hackathon.repository.EventRepository
import com.e1s.hackathon.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskRepository: TaskRepository
) {
    fun getAllEvents(): List<EventDocument> {
        return eventRepository.findAll()
    }

    @Transactional
    fun createEvent(event: EventDocument): EventDocument {
        val saved = eventRepository.save(event)
        // Generate tasks for employees belonging to targeted groups and not blacklisting category
        val employees = employeeRepository.findAll().filter { emp ->
            (event.groups.isEmpty() || emp.groups.any { event.groups.contains(it) }) &&
                emp.categoriesBlacklist.none { it == event.category }
        }
        val tasks = employees.map { employee ->
            TaskDocument(
                event = saved,
                employeeId = employee.id!!
            )
        }
        if (tasks.isNotEmpty()) taskRepository.saveAll(tasks)
        return saved
    }
}

