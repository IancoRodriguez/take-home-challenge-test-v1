package takehomechallenge.dto.notification;

import takehomechallenge.model.ChannelType;
import takehomechallenge.model.Notification;
import takehomechallenge.model.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO para devolver una notificación al cliente
 *
 * ¿Por qué un DTO de respuesta?
 * - Oculta detalles internos de la entidad
 * - Formatea datos apropiadamente para JSON
 * - Puede agregar campos calculados si es necesario
 * - No expone relaciones innecesarias
 *
 * Ejemplo de respuesta:
 * {
 *   "id": 1,
 *   "title": "Welcome!",
 *   "content": "Hello...",
 *   "channel": "EMAIL",
 *   "status": "SENT",
 *   "userEmail": "john.doe@example.com",
 *   "createdAt": "2024-11-07T15:30:00",
 *   "sentAt": "2024-11-07T15:30:05"
 * }
 */
@Schema(description = "Notification response")
@JsonInclude(JsonInclude.Include.NON_NULL) // Oculta campos null (ej: sentAt si no se envió aún)
public class NotificationResponseDto {

    @Schema(description = "Notification ID", example = "1")
    private Long id;

    @Schema(description = "Notification title", example = "Welcome!")
    private String title;

    @Schema(description = "Notification content", example = "Hello...")
    private String content;

    @Schema(description = "Delivery channel", example = "EMAIL")
    private ChannelType channel;

    @Schema(description = "Current status", example = "SENT")
    private NotificationStatus status;

    @Schema(description = "Owner's email", example = "john.doe@example.com")
    private String userEmail;

    @Schema(description = "Creation timestamp", example = "2024-11-07T15:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Sent timestamp (null if not sent yet)", example = "2024-11-07T15:30:05")
    private LocalDateTime sentAt;

    // Constructors
    public NotificationResponseDto() {}

    /**
     * Factory method para convertir entidad a DTO
     *
     * Este es el método más importante del DTO
     * Convierte: Notification (entidad) → NotificationResponseDto (DTO)
     *
     * ¿Por qué un factory method?
     * - Centraliza la lógica de conversión (UN solo lugar)
     * - Evita código duplicado en controllers/services
     * - Fácil de modificar si cambia el formato
     *
     * Uso:
     * NotificationResponseDto dto = NotificationResponseDto.fromEntity(notification);
     *
     * @param notification La entidad a convertir
     * @return DTO listo para devolver al cliente
     */
    public static NotificationResponseDto fromEntity(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.id = notification.getId();
        dto.title = notification.getTitle();
        dto.content = notification.getContent();
        dto.channel = notification.getChannel();
        dto.status = notification.getStatus();
        dto.userEmail = notification.getUserEmail();
        dto.createdAt = notification.getCreatedAt();
        dto.sentAt = notification.getSentAt(); // Puede ser null
        return dto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public ChannelType getChannel() { return channel; }
    public void setChannel(ChannelType channel) { this.channel = channel; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}