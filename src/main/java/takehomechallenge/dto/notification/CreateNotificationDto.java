package takehomechallenge.dto.notification;

import takehomechallenge.model.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new notification")
public class CreateNotificationDto {

    @Schema(
            description = "Notification title",
            example = "Welcome to our platform!",
            required = true,
            minLength = 1,
            maxLength = 255
    )
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(
            description = "Notification content/body",
            example = "Hello! We're excited to have you here. Explore our features.",
            required = true,
            minLength = 1
    )
    @NotBlank(message = "Content is required")
    private String content;

    @Schema(
            description = "Notification channel (EMAIL, SMS, or PUSH)",
            example = "EMAIL",
            required = true,
            allowableValues = {"EMAIL", "SMS", "PUSH"}
    )
    @NotNull(message = "Channel is required")
    private ChannelType channel;

    // Constructors
    public CreateNotificationDto() {}

    public CreateNotificationDto(String title, String content, ChannelType channel) {
        this.title = title;
        this.content = content;
        this.channel = channel;
    }

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public ChannelType getChannel() { return channel; }
    public void setChannel(ChannelType channel) { this.channel = channel; }
}