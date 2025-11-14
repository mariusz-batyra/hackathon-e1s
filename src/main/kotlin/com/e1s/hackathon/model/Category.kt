package com.e1s.hackathon.model

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Represents a task/event category with an optional notification policy and required flag.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Category(
    val name: String,
    val required: Boolean? = null,
    val notificationPolicy: NotificationPolicy? = null
) {
    init {
        require(name.isNotBlank()) { "Category name must not be blank" }
    }
}

