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
                    firstName = "Adelajda",
                    lastName = "Bogucka",
                    groups = listOf(GroupEnum.B2B, GroupEnum.QA),
                    position = "QA Engineer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.SMS),
                    categoriesBlacklist = listOf(Category.PARTY)
                ),
                EmployeeDocument(
                    firstName = "Eryk",
                    lastName = "Machak",
                    groups = listOf(GroupEnum.DEV, GroupEnum.UoP),
                    position = "Senior Developer",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                ),
                EmployeeDocument(
                    firstName = "Ludwika",
                    lastName = "Gibas",
                    groups = listOf(GroupEnum.DEV),
                    position = "Junior Developer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.WHATSAPP)
                ),
                EmployeeDocument(
                    firstName = "Mateusz",
                    lastName = "Drzyzga",
                    groups = listOf(GroupEnum.BACKOFFICE),
                    position = "Backoffice Admin",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                ),
                EmployeeDocument(
                    firstName = "Wincenty",
                    lastName = "Kohut",
                    groups = listOf(GroupEnum.BACKOFFICE),
                    position = "Office assistant",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.MS_TEAMS)
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

        eventService.createEvent(
            EventDocument(
                category = Category.PARTY,
                title = "Friday Beer",
                description = "Piwny piątku w biurze lubelskim o 16:00",
                groups = listOf(GroupEnum.UoP, GroupEnum.B2B)
            )
        )

        // Trzecie wydarzenie bez grup (wszyscy poza blacklist) – Backoffice dostanie zadanie
        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Town Hall Meeting",
                description = "Spotkanie całej firmy",
                groups = emptyList()
            )
        )

        val taskCount = taskRepository.count()

        println("Seed completed: employees=${employees.count()}, events=${eventRepository.count()}, tasks=$taskCount")
        println("Events:\n" + eventRepository.findAll().map { "${it.id} ${it.title}\n" })
        println("Employees:\n" + employeeRepository.findAll().map { "${it.id} ${it.firstName} ${it.lastName}\n" })
        println("Tasks:\n" + taskRepository.findAll().map { "${it.id} for Employee ${it.employeeId} from Event ${it.event.id}\n" })
    }
}
