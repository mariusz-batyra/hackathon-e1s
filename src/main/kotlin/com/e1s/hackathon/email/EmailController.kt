package com.e1s.hackathon.email

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/emails")
class EmailController(
    private val emailFacade: EmailFacade
) {

    @PostMapping("/send-text")
    fun sendTextEmail(@RequestBody request: EmailRequest): ResponseEntity<EmailResponse> {
        return try {
            emailFacade.sendNotification(
                to = request.to,
                subject = request.subject,
                text = request.text
            )
            ResponseEntity.ok(EmailResponse(
                success = true,
                message = "Email sent successfully to ${request.to}"
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(EmailResponse(
                success = false,
                message = "Failed to send email: ${e.message}"
            ))
        }
    }

/*    @PostMapping("/send-html")
    fun sendHtmlEmail(@RequestBody request: HtmlEmailRequest): ResponseEntity<EmailResponse> {
        return try {
            emailFacade.sendHtmlTemplate(
                to = request.to,
                subject = request.subject,
                html = request.html
            )
            ResponseEntity.ok(EmailResponse(
                success = true,
                message = "HTML email sent successfully to ${request.to}"
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(EmailResponse(
                success = false,
                message = "Failed to send email: ${e.message}"
            ))
        }
    }

    @PostMapping("/send-rich")
    fun sendRichEmail(@RequestBody request: RichEmailRequest): ResponseEntity<EmailResponse> {
        return try {
            emailFacade.sendRichEmail(
                to = request.to,
                subject = request.subject,
                text = request.text,
                html = request.html
            )
            ResponseEntity.ok(EmailResponse(
                success = true,
                message = "Rich email sent successfully to ${request.to}"
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(EmailResponse(
                success = false,
                message = "Failed to send email: ${e.message}"
            ))
        }
    }*/
}

data class EmailRequest(
    val to: String,
    val subject: String,
    val text: String
)

data class HtmlEmailRequest(
    val to: String,
    val subject: String,
    val html: String
)

data class RichEmailRequest(
    val to: String,
    val subject: String,
    val text: String,
    val html: String
)

data class EmailResponse(
    val success: Boolean,
    val message: String
)

