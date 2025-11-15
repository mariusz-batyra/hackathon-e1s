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
                    firstName = "Mikołaj",
                    lastName = "Cekut",
                    email = "mikolaj.cekut@edge1s.com",
                    groups = listOf(GroupEnum.B2B, GroupEnum.DEV),
                    position = "Senior Engineer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.SMS),
                    categoriesBlacklist = listOf(Category.PARTY)
                ),
                EmployeeDocument(
                    firstName = "Adelajda",
                    lastName = "Bogucka",
//                    email = "contrabandalbn@gmail.com",
                    groups = listOf(GroupEnum.B2B, GroupEnum.QA),
                    position = "QA Engineer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.SMS),
                    categoriesBlacklist = listOf(Category.PARTY)
                ),
                EmployeeDocument(
                    firstName = "Eryk",
                    lastName = "Machak",
//                    email = "contrabandalbn@gmail.com",
                    groups = listOf(GroupEnum.DEV, GroupEnum.UoP),
                    position = "Senior Developer",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                ),
                EmployeeDocument(
                    firstName = "Ludwika",
                    lastName = "Gibas",
//                    email = "contrabandalbn@gmail.com",
                    groups = listOf(GroupEnum.DEV),
                    position = "Junior Developer",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.WHATSAPP)
                ),
                EmployeeDocument(
                    firstName = "Mateusz",
                    lastName = "Drzyzga",
//                    email = "contrabandalbn@gmail.com",
                    groups = listOf(GroupEnum.BACKOFFICE),
                    position = "Backoffice Admin",
                    notificationChannels = listOf(NotificationChannel.EMAIL)
                ),
                EmployeeDocument(
                    firstName = "Wincenty",
                    lastName = "Kohut",
//                    email = "contrabandalbn@gmail.com",
                    groups = listOf(GroupEnum.BACKOFFICE),
                    position = "Office assistant",
                    notificationChannels = listOf(NotificationChannel.EMAIL, NotificationChannel.MS_TEAMS)
                )
            )
        )

        // Wydarzenie demo które wygeneruje Taski dla DEV i QA (Anna, Bartek)
        eventService.createEvent(
            EventDocument(
                category = Category.SECURITY,
                title = "Szkolenie bezpieczenstwa",
                description = "Obowiazkowe szkolenie bezpieczenstwa dla zespołow DEV i QA",
                groups = listOf(GroupEnum.DEV, GroupEnum.QA)
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.PARTY,
                title = "Friday Beer",
                description = "Piwny piatku w biurze lubelskim o 16:00",
                groups = listOf(GroupEnum.UoP, GroupEnum.B2B)
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.PARTY,
                title = "Sniadanie w biurze",
                description = "Zapraszamy na wspolne sniadanie w biurze w czwartek o 9:00.",
                groups = listOf(GroupEnum.UoP, GroupEnum.B2B)
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Town Hall Meeting",
                description = "Spotkanie calej firmy",
                groups = emptyList()
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Przypomnienie o fakturze",
                description = "Przypominamy o koniecznosci przeslania faktury za uslugi informatyczne.",
                groups = emptyList()
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Zapisy na spotkanie Swiąteczne",
                description = "Zapisy na firmowe spotkanie swiąteczne juz trwaja! Zarezerwuj swoje miejsce juz dzis.",
                groups = emptyList()
            )
        )

        eventService.createEvent(
            EventDocument(
                category = Category.GENERAL,
                title = "Zapisy na siatkowke",
                description = "Zapisy na siatkowke w każdą środę o 20:00 na Akademos Lublin. Dołącz do drużyny!",
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
