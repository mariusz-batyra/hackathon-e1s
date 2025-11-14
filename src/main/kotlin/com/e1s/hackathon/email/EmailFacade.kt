package com.e1s.hackathon.email

import org.springframework.stereotype.Component

/**
 * Facade for email operations.
 *
 * This facade provides a simplified interface for email sending operations,
 * hiding the complexity of SMTP configuration and email creation.
 *
 * Following SOLID principles:
 * - Single Responsibility: Provides unified email interface
 * - Interface Segregation: Simple, focused API for clients
 * - Dependency Inversion: Depends on EmailService abstraction
 *
 * Use this facade in your application code instead of calling EmailService directly.
 * This allows for easier testing and potential future changes to email providers.
 */
@Component
class EmailFacade(
    private val emailService: EmailService
) {
    /**
     * Sends a plain text notification email.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param text Plain text message content
     */
    fun sendNotification(to: String, subject: String, text: String) {
        emailService.sendPlainText(to, subject, text)
    }

    /**
     * Sends an HTML template email.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param html HTML content
     */
    fun sendHtmlTemplate(to: String, subject: String, html: String) {
        emailService.sendHtml(to, subject, html)
    }

    /**
     * Sends an email with both plain text and HTML versions.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param text Plain text version (for email clients that don't support HTML)
     * @param html HTML version
     */
    fun sendRichEmail(to: String, subject: String, text: String, html: String) {
        emailService.sendMultipart(to, subject, text, html)
    }
}

