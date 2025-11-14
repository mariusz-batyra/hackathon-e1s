package com.e1s.hackathon.email

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Configuration class to enable email properties binding.
 */
@Configuration
@EnableConfigurationProperties(EmailProperties::class)
class EmailConfig

