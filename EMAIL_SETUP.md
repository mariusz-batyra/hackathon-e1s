# Email Service - Konfiguracja SMTP (Gmail)

## Przegląd

Serwis wysyłania emaili zintegrowany z dowolnym serwerem SMTP. Domyślnie skonfigurowany dla Gmail, ale działa z każdym SMTP (Outlook, SendGrid, AWS SES, itp.).

## Architektura

Implementacja oparta na Spring Boot Mail z następującymi komponentami:

- **EmailProperties** - konfiguracja nadawcy
- **EmailService** - główna logika wysyłania emaili przez SMTP
- **EmailFacade** - uproszczony interfejs dla aplikacji
- **EmailController** - REST API do testowania

## Konfiguracja Gmail SMTP

### Krok 1: Włącz 2-Factor Authentication w Gmail

1. Przejdź do [Google Account Security](https://myaccount.google.com/security)
2. Włącz "2-Step Verification"

### Krok 2: Wygeneruj App Password

1. Przejdź do [App Passwords](https://myaccount.google.com/apppasswords)
2. Wybierz "Mail" i "Other" (Custom name)
3. Nazwij to np. "Hackathon App"
4. Kliknij "Generate"
5. Skopiuj wygenerowane hasło (16 znaków bez spacji)

⚠️ **WAŻNE**: To hasło aplikacji będzie wyświetlone tylko raz! Zapisz je bezpiecznie.

### Krok 3: Skonfiguruj zmienne środowiskowe

```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=twoj-email@gmail.com
export MAIL_PASSWORD=tvoj-app-password-16-znakow
export EMAIL_FROM=twoj-email@gmail.com
export EMAIL_FROM_NAME="Hackathon Application"
```

Lub utwórz plik `.env` w głównym katalogu projektu:

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=twoj-email@gmail.com
MAIL_PASSWORD=tvoj-app-password
EMAIL_FROM=twoj-email@gmail.com
EMAIL_FROM_NAME=Hackathon Application
```

## Konfiguracja dla innych providerów

### Outlook/Office 365

```properties
spring.mail.host=smtp.office365.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

### Yahoo Mail

```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

### SendGrid

```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=your-sendgrid-api-key
```

### AWS SES

```properties
spring.mail.host=email-smtp.us-east-1.amazonaws.com
spring.mail.port=587
spring.mail.username=your-smtp-username
spring.mail.password=your-smtp-password
```

### Własny SMTP Server

```properties
spring.mail.host=smtp.yourserver.com
spring.mail.port=587
spring.mail.username=your-username
spring.mail.password=your-password
```

## Użycie w kodzie

### Przykład 1: Wysyłanie prostego emaila

```kotlin
@Service
class NotificationService(
    private val emailFacade: EmailFacade
) {
    fun sendWelcomeEmail(userEmail: String, userName: String) {
        emailFacade.sendNotification(
            to = userEmail,
            subject = "Witaj w naszej aplikacji!",
            text = "Cześć $userName, dziękujemy za rejestrację!"
        )
    }
}
```

### Przykład 2: Wysyłanie HTML emaila

```kotlin
@Service
class ReportService(
    private val emailFacade: EmailFacade
) {
    fun sendMonthlyReport(userEmail: String, stats: UserStats) {
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; }
                    .header { background: #4CAF50; color: white; padding: 20px; }
                    .stats { padding: 20px; }
                    .stat-item { margin: 10px 0; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Twój miesięczny raport</h1>
                </div>
                <div class="stats">
                    <div class="stat-item">
                        <strong>Zadania wykonane:</strong> ${stats.tasksCompleted}
                    </div>
                    <div class="stat-item">
                        <strong>Przepracowane godziny:</strong> ${stats.hoursWorked}
                    </div>
                    <div class="stat-item">
                        <strong>Punkty:</strong> ${stats.points}
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
        
        emailFacade.sendHtmlTemplate(
            to = userEmail,
            subject = "Twój miesięczny raport",
            html = html
        )
    }
}
```

### Przykład 3: Email z wersją tekstową i HTML

```kotlin
@Service
class InvoiceService(
    private val emailFacade: EmailFacade
) {
    fun sendInvoice(userEmail: String, invoice: Invoice) {
        val textVersion = """
            Faktura #${invoice.number}
            Data: ${invoice.date}
            Kwota: ${invoice.amount} PLN
            
            Pozycje:
            ${invoice.items.joinToString("\n") { "- ${it.name}: ${it.price} PLN" }}
        """.trimIndent()
        
        val htmlVersion = """
            <html>
            <body>
                <h2>Faktura #${invoice.number}</h2>
                <p><strong>Data:</strong> ${invoice.date}</p>
                <p><strong>Kwota:</strong> ${invoice.amount} PLN</p>
                <table border="1">
                    <tr><th>Pozycja</th><th>Cena</th></tr>
                    ${invoice.items.joinToString("") { 
                        "<tr><td>${it.name}</td><td>${it.price} PLN</td></tr>" 
                    }}
                </table>
            </body>
            </html>
        """.trimIndent()
        
        emailFacade.sendRichEmail(
            to = userEmail,
            subject = "Faktura #${invoice.number}",
            text = textVersion,
            html = htmlVersion
        )
    }
}
```

## Testowanie przez REST API

### Wyślij email tekstowy

```bash
curl -X POST http://localhost:8080/api/emails/send-text \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email",
    "text": "To jest testowa wiadomość"
  }'
```

### Wyślij email HTML

```bash
curl -X POST http://localhost:8080/api/emails/send-html \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test HTML Email",
    "html": "<h1>Witaj!</h1><p>To jest <strong>testowa</strong> wiadomość.</p>"
  }'
```

### Wyślij email z obiema wersjami

```bash
curl -X POST http://localhost:8080/api/emails/send-rich \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Rich Email",
    "text": "Wersja tekstowa wiadomości",
    "html": "<h1>Wersja HTML</h1><p>Z formatowaniem</p>"
  }'
```

### Sprawdź status serwisu

```bash
curl http://localhost:8080/api/emails/health
```

## Docker Compose

Plik `docker-compose.yml` już zawiera konfigurację zmiennych środowiskowych dla emaila:

```yaml
environment:
  MAIL_HOST: ${MAIL_HOST}
  MAIL_PORT: ${MAIL_PORT}
  MAIL_USERNAME: ${MAIL_USERNAME}
  MAIL_PASSWORD: ${MAIL_PASSWORD}
  EMAIL_FROM: ${EMAIL_FROM}
  EMAIL_FROM_NAME: ${EMAIL_FROM_NAME}
```

Utwórz plik `.env` obok `docker-compose.yml`:

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
EMAIL_FROM=your-email@gmail.com
EMAIL_FROM_NAME=Hackathon App
```

Następnie uruchom:

```bash
docker-compose up -d
```

## Bezpieczeństwo

✅ **Dobre praktyki:**
- Używaj App Password zamiast głównego hasła konta
- Przechowuj credentials w zmiennych środowiskowych
- Nigdy nie commituj credentials do repozytorium
- Walidacja wszystkich danych wejściowych
- Logowanie wszystkich operacji email

⚠️ **Uwagi:**
- Gmail ma limity wysyłki: ~500 emaili/dzień dla darmowych kont
- Implementuj rate limiting w produkcji
- Monitoruj logi pod kątem błędów SMTP
- Rozważ użycie dedykowanego serwisu (SendGrid, AWS SES) dla wysokiego ruchu

## Limity Gmail

- **Dziennie**: ~500 emaili (konta darmowe), ~2000 (Google Workspace)
- **Na minutę**: ~100 emaili
- **Rozmiar**: Max 25 MB (z załącznikami)

## Troubleshooting

### Błąd: "Authentication failed"
- Sprawdź czy używasz App Password, nie głównego hasła
- Upewnij się że 2FA jest włączony w Gmail
- Sprawdź czy username to pełny adres email

### Błąd: "Could not connect to SMTP host"
- Sprawdź czy port 587 nie jest zablokowany przez firewall
- Spróbuj portu 465 z SSL: `spring.mail.port=465` i `spring.mail.properties.mail.smtp.ssl.enable=true`

### Błąd: "Timeout"
- Zwiększ timeout: `spring.mail.properties.mail.smtp.timeout=10000`
- Sprawdź połączenie internetowe

### Email idzie do SPAM
- Skonfiguruj SPF, DKIM i DMARC dla swojej domeny
- Unikaj słów-kluczy spamowych w tytule
- Dodaj opcję unsubscribe dla masowych wysyłek
- Rozważ użycie dedykowanego serwisu email

## Monitoring i Logging

Wszystkie operacje email są logowane:

```
INFO  - Sending plain text email to: user@example.com
INFO  - Plain text email sent successfully to: user@example.com
ERROR - Failed to send plain text email to: user@example.com
```

W produkcji dodaj:
- Metryki (Micrometer) dla liczby wysłanych emaili
- Alerty przy wysokim współczynniku błędów
- Dashboard z statystykami email

## Rozszerzenia (TODO)

- [ ] Dodać obsługę załączników
- [ ] Template engine (Thymeleaf/FreeMarker)
- [ ] Queue system dla asynchronicznego wysyłania
- [ ] Retry mechanism z exponential backoff
- [ ] Circuit breaker dla resilience
- [ ] Metryki i monitoring
- [ ] Unsubscribe mechanism
- [ ] Email tracking (open/click rates)

## Przykładowy workflow produkcyjny

```kotlin
@Service
class OrderService(
    private val emailFacade: EmailFacade,
    private val orderRepository: OrderRepository
) {
    @Async
    @Transactional
    fun processOrder(order: Order) {
        // Save order
        orderRepository.save(order)
        
        // Send confirmation email asynchronously
        try {
            emailFacade.sendHtmlTemplate(
                to = order.customerEmail,
                subject = "Potwierdzenie zamówienia #${order.id}",
                html = generateOrderConfirmationHtml(order)
            )
        } catch (e: Exception) {
            // Log error but don't fail the order
            logger.error("Failed to send order confirmation", e)
            // Optionally: add to retry queue
        }
    }
}
```

## Wsparcie

W przypadku problemów:
1. Sprawdź logi aplikacji
2. Zweryfikuj konfigurację SMTP
3. Przetestuj połączenie z serwerem SMTP
4. Sprawdź dokumentację dostawcy email

---

**Gotowe!** Twój serwis email jest skonfigurowany i gotowy do użycia. 🚀

