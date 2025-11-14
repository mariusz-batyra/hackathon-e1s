package com.e1s.hackathon.email

import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val emailProperties: EmailProperties
) {
    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    /**
     * Sends a plain text email.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Plain text email body
     * @throws IllegalArgumentException if any parameter is blank
     * @throws EmailSendingException if email sending fails
     */
    fun sendPlainText(to: String, subject: String, body: String) {
        validateInputs(to, subject, body)
        logger.info("Sending plain text email to: $to")

        try {
            val message = createMimeMessage(to, subject, body, isHtml = false)
            mailSender.send(message)
            logger.info("Plain text email sent successfully to: $to")
        } catch (e: Exception) {
            logger.error("Failed to send plain text email to: $to", e)
            throw EmailSendingException("Failed to send email to $to", e)
        }
    }

    /**
     * Sends an HTML email.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param htmlBody HTML email body
     * @throws IllegalArgumentException if any parameter is blank
     * @throws EmailSendingException if email sending fails
     */
    fun sendHtml(to: String, subject: String, htmlBody: String) {
        validateInputs(to, subject, htmlBody)
        logger.info("Sending HTML email to: $to")

        try {
            val message = createMimeMessage(to, subject, htmlBody, isHtml = true)
            mailSender.send(message)
            logger.info("HTML email sent successfully to: $to")
        } catch (e: Exception) {
            logger.error("Failed to send HTML email to: $to", e)
            throw EmailSendingException("Failed to send email to $to", e)
        }
    }

    /**
     * Sends an email with both plain text and HTML versions.
     * Email clients will show HTML version if supported, otherwise plain text.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param textBody Plain text version
     * @param htmlBody HTML version
     */
    fun sendMultipart(to: String, subject: String, textBody: String, htmlBody: String) {
        validateInputs(to, subject, textBody)
        require(StringUtils.isNotBlank(htmlBody)) { "HTML body is required" }
        logger.info("Sending multipart email to: $to")

        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setFrom(InternetAddress(emailProperties.from, emailProperties.fromName))
            helper.setTo(InternetAddress(to))
            helper.setSubject(subject)
            helper.setText(textBody, htmlBody)

            mailSender.send(message)
            logger.info("Multipart email sent successfully to: $to")
        } catch (e: Exception) {
            logger.error("Failed to send multipart email to: $to", e)
            throw EmailSendingException("Failed to send email to $to", e)
        }
    }

    /**
     * Creates a MIME message for email.
     *
     * @param to Recipient email
     * @param subject Email subject
     * @param content Email content (plain text or HTML)
     * @param isHtml Whether content is HTML or plain text
     * @return Constructed MimeMessage
     */
    private fun createMimeMessage(
        to: String,
        subject: String,
        content: String,
        isHtml: Boolean
    ): MimeMessage {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(InternetAddress(emailProperties.from, emailProperties.fromName))
        helper.setTo(InternetAddress(to))
        helper.setSubject(subject)
        helper.setText(content, isHtml)

        return message
    }

    /**
     * Validates email input parameters.
     *
     * @throws IllegalArgumentException if validation fails
     */
    private fun validateInputs(to: String, subject: String, body: String) {
        require(StringUtils.isNotBlank(to)) { "Recipient email address is required" }
        require(StringUtils.isNotBlank(subject)) { "Email subject is required" }
        require(StringUtils.isNotBlank(body)) { "Email body is required" }
    }
}

/**
 * Custom exception for email sending failures.
 */
class EmailSendingException(message: String, cause: Throwable) : RuntimeException(message, cause)

