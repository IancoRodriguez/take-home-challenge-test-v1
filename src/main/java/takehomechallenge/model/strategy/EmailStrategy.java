package takehomechallenge.model.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import takehomechallenge.model.Notification;

@Component
public class EmailStrategy implements IChannelStrategy{

    private static final Logger log = LoggerFactory.getLogger(EmailStrategy.class);

    private final IEmailSender emailSender; // Spring inyecta la correcta según profile

    public EmailStrategy(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(Notification notification) {
        log.info("📧 EMAIL Strategy - Starting send process");

        // 1. Validaciones específicas de email
        validateEmailContent(notification.getContent());

        // 2. Generar template HTML profesional
        String htmlContent = generateHtmlTemplate(notification);

        // 3. Delegar envío (simulado o real según profile)
        emailSender.send(
                notification.getUserEmail(),
                notification.getTitle(),
                htmlContent
        );

        log.info("📧 EMAIL Strategy - Send process completed");
    }

    private void validateEmailContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Email content cannot be empty");
        }
        if (content.length() > 5000) {
            throw new IllegalArgumentException("Email content too long (max 5000 characters)");
        }
    }

    private String generateHtmlTemplate(Notification notification) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        overflow: hidden;
                    }
                    .header {
                        background-color: #007bff;
                        color: #ffffff;
                        padding: 30px;
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                    }
                    .content {
                        padding: 30px;
                        line-height: 1.6;
                        color: #333333;
                    }
                    .footer {
                        background-color: #f8f9fa;
                        padding: 20px;
                        text-align: center;
                        font-size: 12px;
                        color: #6c757d;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>%s</h1>
                    </div>
                    <div class="content">
                        %s
                    </div>
                    <div class="footer">
                        <p>This is an automated notification. Please do not reply.</p>
                        <p>&copy; 2024 Notifications System. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(notification.getTitle(), notification.getContent());
    }
}
