package com.e1s.hackathon.service

import com.e1s.hackathon.email.EmailFacade
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
    private val taskRepository: TaskRepository,
    private val emailFacade: EmailFacade
) {
    fun getAllEvents(): List<EventDocument> {
        return eventRepository.findAll()
    }

    fun getEventTasks(eventId: String): List<TaskDocument> = taskRepository.findByEventId(eventId)

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
        if (tasks.isNotEmpty()) {
            taskRepository.saveAll(tasks)

            // Send email notification to each employee
            employees.forEach { employee ->
                employee.email?.let { email ->
                    try {
                        emailFacade.sendNotification(
                            to = email,
                            subject = "New Task: ${saved.title}",
                            text = """
                                Hello ${employee.firstName} ${employee.lastName},
                                
                                You have been assigned a new task:
                                
                                Event: ${saved.title}
                                Category: ${saved.category}
                                Description: ${saved.description}
                                
                                Please review and complete this task at your earliest convenience.
                                
                                Best regards,
                                Event Management System
                            """.trimIndent()
                        )
                    } catch (e: Exception) {
                        // Log error but don't fail the transaction
                        println("Failed to send email to ${employee.email}: ${e.message}")
                    }
                }
            }
        }
        return saved
    }

    fun getEmployeesMap(): Map<String, com.e1s.hackathon.model.EmployeeDocument> =
        employeeRepository.findAll().associateBy { it.id!! }
}
