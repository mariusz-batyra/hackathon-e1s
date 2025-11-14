package com.e1s.hackathon.model

/**
 * Represents a task/event category with required flag and notification policy.
 */
enum class Category(
    val required: Boolean,
    val notificationPolicy: NotificationPolicy
) {
    SECURITY(required = true, notificationPolicy = NotificationPolicy.STRICT),
    SALES(required = false, notificationPolicy = NotificationPolicy.MEDIUM),
    GENERAL(required = false, notificationPolicy = NotificationPolicy.RELAXED),
    ONBOARDING(required = true, notificationPolicy = NotificationPolicy.STRICT),
    TRAINING(required = false, notificationPolicy = NotificationPolicy.MEDIUM),
    HR(required = false, notificationPolicy = NotificationPolicy.MEDIUM),
    IT(required = true, notificationPolicy = NotificationPolicy.STRICT),
    COMPLIANCE(required = true, notificationPolicy = NotificationPolicy.STRICT)
}


