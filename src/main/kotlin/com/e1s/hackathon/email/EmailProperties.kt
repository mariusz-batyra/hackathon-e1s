package com.e1s.hackathon.email
import org.springframework.boot.context.properties.ConfigurationProperties
/**
 * Configuration properties for email sender.
 * 
 * These properties are loaded from application.properties with prefix "email".
 */
@ConfigurationProperties(prefix = "email")
data class EmailProperties(
    val from: String,
    val fromName: String = "Hackathon App"
)
