package com.e1s.hackathon.service

import com.e1s.hackathon.model.*
import com.e1s.hackathon.repository.EmployeeRepository
import com.e1s.hackathon.repository.TaskRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class StatisticsService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository
) {
    // Rozszerzone: dodano rejectedPercentage i rejected count
    data class CategoryCompletionRate(val category: Category, val donePercentage: Double, val rejectedPercentage: Double, val total: Long, val done: Long, val rejected: Long)
    data class ChannelEffectiveness(val channel: NotificationChannel, val completionRate: Double, val tasks: Long)
    data class ChannelPreferenceSlice(val channel: NotificationChannel, val percentage: Double, val employees: Long)

    data class StatsResponse(
        val overallDonePercentage: Double,
        val overallRejectedPercentage: Double, // NOWE: procent odrzuconych zadań
        val pendingPercentage: Double,
        val expiringSoonPercentage: Double,
        val categoryRates: List<CategoryCompletionRate>,
        val alertEmployees: List<String>,
        val totalDeliveredNotifications: Long,
        val totalUniqueClicks: Long,
        val averageCompletionHours: Double?,
        val channelEffectiveness: List<ChannelEffectiveness>,
        val channelPreferences: List<ChannelPreferenceSlice>
    )

    fun getStats(expiringSoonThresholdHours: Long = 24): StatsResponse {
        val tasks = taskRepository.findAll()
        if (tasks.isEmpty()) {
            return emptyStats()
        }
        val total = tasks.size.toLong()
        val doneTasks = tasks.count { it.status == TaskStatus.DONE }.toLong()
        val rejectedTasks = tasks.count { it.status == TaskStatus.REJECTED }.toLong()
        val pendingTasks = tasks.count { it.status == TaskStatus.NEW }.toLong()
        val now = Instant.now()
        val expiringSoon = tasks.count { it.status == TaskStatus.NEW && it.deadline != null && Duration.between(now, it.deadline).toHours() in 0..expiringSoonThresholdHours }.toLong()

        val overallDonePct = pct(doneTasks, total)
        val overallRejectedPct = pct(rejectedTasks, total)
        val pendingPct = pct(pendingTasks, total)
        val expiringPct = pct(expiringSoon, total)

        val categoryRates = tasks.groupBy { it.event.category }.map { (cat, catTasks) ->
            val catTotal = catTasks.size.toLong()
            val catDone = catTasks.count { it.status == TaskStatus.DONE }.toLong()
            val catRejected = catTasks.count { it.status == TaskStatus.REJECTED }.toLong()
            CategoryCompletionRate(cat, pct(catDone, catTotal), pct(catRejected, catTotal), catTotal, catDone, catRejected)
        }.sortedBy { it.category.name }

        val alertEmployees = tasks.filter { it.status == TaskStatus.NEW && it.deadline != null && it.deadline.isBefore(now) }
            .map { it.employeeId }
            .distinct()

        val totalDelivered = tasks.sumOf { it.deliveredNotifications.toLong() }
        val totalClicks = tasks.sumOf { it.uniqueClicks.toLong() }

        val completionDurations = tasks.filter { it.status == TaskStatus.DONE && it.doneAt != null }
            .map { Duration.between(it.createdAt, it.doneAt).toHours().toDouble() }
        val avgHours = if (completionDurations.isNotEmpty()) completionDurations.average() else null

        val channelEffectiveness = tasks.filter { it.primaryChannel != null }
            .groupBy { it.primaryChannel!! }
            .map { (ch, chTasks) ->
                val chTotal = chTasks.size.toLong()
                val chDone = chTasks.count { it.status == TaskStatus.DONE }.toLong()
                ChannelEffectiveness(ch, pct(chDone, chTotal), chTotal)
            }
            .sortedByDescending { it.completionRate }

        val employees = employeeRepository.findAll()
        val channelPreferences = NotificationChannel.values().map { ch ->
            val count = employees.count { it.notificationChannels.contains(ch) }.toLong()
            ChannelPreferenceSlice(ch, pct(count, employees.size.toLong()), count)
        }.sortedByDescending { it.percentage }

        return StatsResponse(
            overallDonePercentage = overallDonePct,
            overallRejectedPercentage = overallRejectedPct,
            pendingPercentage = pendingPct,
            expiringSoonPercentage = expiringPct,
            categoryRates = categoryRates,
            alertEmployees = alertEmployees,
            totalDeliveredNotifications = totalDelivered,
            totalUniqueClicks = totalClicks,
            averageCompletionHours = avgHours,
            channelEffectiveness = channelEffectiveness,
            channelPreferences = channelPreferences
        )
    }

    private fun pct(part: Long, total: Long): Double = if (total == 0L) 0.0 else (part.toDouble() / total.toDouble()) * 100.0

    private fun emptyStats() = StatsResponse(
        overallDonePercentage = 0.0,
        overallRejectedPercentage = 0.0,
        pendingPercentage = 0.0,
        expiringSoonPercentage = 0.0,
        categoryRates = emptyList(),
        alertEmployees = emptyList(),
        totalDeliveredNotifications = 0,
        totalUniqueClicks = 0,
        averageCompletionHours = null,
        channelEffectiveness = emptyList(),
        channelPreferences = emptyList()
    )
}
