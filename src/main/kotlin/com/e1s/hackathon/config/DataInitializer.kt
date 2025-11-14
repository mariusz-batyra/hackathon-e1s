package com.e1s.hackathon.config

import com.e1s.hackathon.model.Category
import com.e1s.hackathon.model.EmployeeDocument
import com.e1s.hackathon.model.EventDocument
import com.e1s.hackathon.model.GroupEnum
import com.e1s.hackathon.model.NotificationChannel
import com.e1s.hackathon.repository.EmployeeRepository
import com.e1s.hackathon.repository.EventRepository
import com.e1s.hackathon.repository.TaskRepository
import com.e1s.hackathon.service.EventService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    @Bean
    fun initDatabase(
        employeeRepository: EmployeeRepository,
        eventRepository: EventRepository,
        taskRepository: TaskRepository,
        eventService: EventService
    ) = CommandLineRunner {
        employeeRepository.deleteAll()
        eventRepository.deleteAll()
        taskRepository.deleteAll()

        // Pracownicy demo
        val employees = employeeRepository.saveAll(
            listOf(
                EmployeeDocument(
                    firstName = "Anna",
                    lastName = "Dev",
                    groups = listOf(GroupEnum.DEV, GroupEnum.QA),
                    position = "Senior Developer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.SMS)
                ),
                EmployeeDocument(
                    firstName = "Bartek",
                    lastName = "QA",
                    groups = listOf(GroupEnum.QA),
                    position = "QA Engineer",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                ),
                EmployeeDocument(
                    firstName = "Celina",
                    lastName = "Sales",
                    groups = listOf(GroupEnum.SALES),
                    position = "Sales Specialist",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.WHATSAPP)
                ),
                EmployeeDocument(
                    firstName = "Darek",
                    lastName = "Back",
                    groups = listOf(GroupEnum.BACKOFFICE),
                    position = "Backoffice Admin",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                )
            )
        )

        // Wydarzenie demo które wygeneruje Taski dla DEV i QA (Anna, Bartek)
        val onboardingEvent = EventDocument(
            category = Category.SECURITY,
            title = "Quarterly Security Training",
            description = "Obowiązkowe szkolenie bezpieczeństwa dla zespołów DEV i QA",
            groups = listOf(GroupEnum.DEV, GroupEnum.QA)
        )
        val savedEvent = eventService.createEvent(onboardingEvent)

        // Drugie wydarzenie skierowane do Sales (Celina)
        eventService.createEvent(
            EventDocument(
                category = Category.SALES,
                title = "New Product Launch Brief",
                description = "Informacja o nowym produkcie dla działu sprzedaży",
                groups = listOf(GroupEnum.SALES)
            )
        )

        // Trzecie wydarzenie bez grup (wszyscy poza blacklist) – Backoffice dostanie zadanie
        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Company All Hands",
                description = "Spotkanie całej firmy",
                groups = emptyList()
            )
        )

        val taskCount = taskRepository.count()

        println("Seed completed: employees=${employees.count()}, events=${eventRepository.count()}, tasks=$taskCount")
        println("Example event id: ${savedEvent.id}")
        println("Example employee id (Anna): ${employees.first().id}")
        println("Example task id: " + taskRepository.findByEmployeeId(employees.first().id!!))
    }
}
