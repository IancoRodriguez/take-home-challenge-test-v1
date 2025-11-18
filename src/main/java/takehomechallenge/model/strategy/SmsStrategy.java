package takehomechallenge.model.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import takehomechallenge.model.Notification;

@Component
public class SmsStrategy implements IChannelStrategy{
    private static final Logger log = LoggerFactory.getLogger(SmsStrategy.class);
    private static final int MAX_SMS_LENGTH = 160;

    @Override
    public void send(Notification notification) {
        log.info("📱 SMS Strategy - Starting send process");

        // Validar y limitar a 160 caracteres
        String content = limitTo160Chars(notification.getContent());

        // Simular envío SMS
        log.info("📱 ========================================");
        log.info("📱 [SIMULATION] Sending SMS...");
        log.info("📱 ========================================");
        log.info("📱 To: {}", notification.getUserEmail()); // En realidad sería phoneNumber
        log.info("📱 Content: {}", content);
        log.info("📱 Length: {}/160 characters", content.length());
        log.info("📱 ========================================");

        // Simular delay de red (300ms)
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("SMS sending interrupted", e);
        }

        log.info("✅ [SIMULATION] SMS sent successfully!");
        log.info("📱 ========================================\n");

        log.info("📱 SMS Strategy - Send process completed");
    }

    private String limitTo160Chars(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("SMS content cannot be empty");
        }

        if (content.length() <= MAX_SMS_LENGTH) {
            return content;
        }

        // Truncar y agregar "..."
        String truncated = content.substring(0, MAX_SMS_LENGTH - 3) + "...";
        log.warn("📱 SMS content truncated from {} to {} characters",
                content.length(), truncated.length());

        return truncated;
    }
}
