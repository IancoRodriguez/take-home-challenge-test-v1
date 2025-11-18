package takehomechallenge.model.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import takehomechallenge.model.Notification;

@Component
public class PushStrategy implements IChannelStrategy{

    private static final Logger log = LoggerFactory.getLogger(PushStrategy.class);

    @Override
    public void send(Notification notification) {
        log.info("📲 PUSH Strategy - Starting send process");

        // Validar restricciones de push
        validatePushPayload(notification);

        // Generar payload JSON (formato FCM)
        String payload = generatePushPayload(notification);

        // Simular envío
        log.info("📲 ========================================");
        log.info("📲 [SIMULATION] Sending push notification...");
        log.info("📲 ========================================");
        log.info("📲 To: {}", notification.getUserEmail()); // En realidad sería deviceToken
        log.info("📲 Title: {}", notification.getTitle());
        log.info("📲 Body: {}", notification.getContent());
        log.info("📲 Payload size: {} bytes", payload.length());
        log.info("📲 ========================================");

        // Simular delay (más rápido que email y SMS)
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Push notification sending interrupted", e);
        }

        log.info("✅ [SIMULATION] Push notification sent successfully!");
        log.info("📲 ========================================\n");

        log.info("📲 PUSH Strategy - Send process completed");
    }

    private void validatePushPayload(Notification notification) {
        if (notification.getTitle() == null || notification.getTitle().isBlank()) {
            throw new IllegalArgumentException("Push notification title cannot be empty");
        }

        if (notification.getContent() == null || notification.getContent().isBlank()) {
            throw new IllegalArgumentException("Push notification body cannot be empty");
        }

        // Validar longitud del título (Android/iOS tienen límites)
        if (notification.getTitle().length() > 50) {
            throw new IllegalArgumentException(
                    "Push notification title too long (max 50 characters, got "
                            + notification.getTitle().length() + ")"
            );
        }

        // Validar longitud del body
        if (notification.getContent().length() > 240) {
            throw new IllegalArgumentException(
                    "Push notification body too long (max 240 characters, got "
                            + notification.getContent().length() + ")"
            );
        }
    }

    private String generatePushPayload(Notification notification) {
        // Este es el formato REAL de Firebase Cloud Messaging (FCM)
        // En producción, este JSON se enviaría a la API de Firebase
        return String.format("""
            {
              "to": "%s",
              "notification": {
                "title": "%s",
                "body": "%s",
                "sound": "default",
                "badge": "1"
              },
              "priority": "high",
              "data": {
                "notificationId": "%d",
                "timestamp": "%s"
              }
            }
            """,
                notification.getUserEmail(), // En realidad sería deviceToken
                escapeJson(notification.getTitle()),
                escapeJson(notification.getContent()),
                notification.getId() != null ? notification.getId() : 0,
                notification.getCreatedAt()
        );
    }

    // Escapa caracteres especiales para JSON (previene inyección de código)
    private String escapeJson(String text) {
        if (text == null) return "";
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
