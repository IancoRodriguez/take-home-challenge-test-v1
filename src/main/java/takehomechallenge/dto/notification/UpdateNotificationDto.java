package takehomechallenge.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing notification")
public class UpdateNotificationDto {

    @Schema(
            description = "New notification title",
            example = "Updated: Welcome to our platform!",
            minLength = 1,
            maxLength = 255
    )
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(
            description = "New notification content/body",
            example = "Updated content with more information.",
            minLength = 1
    )
    @NotBlank(message = "Content is required")
    private String content;

    // Constructors
    public UpdateNotificationDto() {}

    public UpdateNotificationDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}