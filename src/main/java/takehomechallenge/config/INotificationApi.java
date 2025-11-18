package takehomechallenge.config;

import takehomechallenge.dto.notification.CreateNotificationDto;
import takehomechallenge.dto.notification.UpdateNotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface API para notificaciones (documentación Swagger)
 *
 * ¿Por qué separar la interface del controller?
 * - Controller queda limpio (solo lógica)
 * - Documentación centralizada en la interface
 * - Mismo patrón que IAuthApi
 *
 * Todas las rutas están protegidas con JWT (@SecurityRequirement)
 */
@Tag(
        name = "Notifications",
        description = "Endpoints for managing notifications. All endpoints require JWT authentication."
)
@SecurityRequirement(name = "bearerAuth") // Todas las rutas requieren JWT
public interface INotificationApi {

    /**
     * Crear una notificación y enviarla de forma asíncrona
     *
     * Flujo:
     * 1. Usuario envía: title, content, channel
     * 2. Sistema crea notificación (status=PENDING)
     * 3. Sistema devuelve respuesta INMEDIATA
     * 4. En background, envía la notificación
     * 5. Status cambia: PENDING → SENDING → SENT/FAILED
     */
    @Operation(
            summary = "Create and send a notification",
            description = """
            Creates a new notification and sends it asynchronously in the background.
            
            The notification is created immediately with status PENDING and returned to the user.
            The actual sending happens in a background thread and updates the status to:
            - SENDING: While being sent
            - SENT: If sent successfully
            - FAILED: If sending failed
            
            You can query the notification later to check its status.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Notification created successfully (status=PENDING, sending in background)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "title": "Welcome!",
                          "content": "Hello John, welcome to our platform!",
                          "channel": "EMAIL",
                          "status": "PENDING",
                          "userEmail": "john.doe@example.com",
                          "createdAt": "2024-11-07T15:30:00"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "title": "Title is required",
                          "channel": "Channel is required"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "error": "Unauthorized"
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<?> create(
            @Valid @RequestBody CreateNotificationDto dto,
            Authentication authentication
    );

    /**
     * Listar todas las notificaciones del usuario autenticado
     */
    @Operation(
            summary = "Get all notifications",
            description = """
            Returns all notifications belonging to the authenticated user.
            
            Each notification includes:
            - id: Unique identifier
            - title: Notification title
            - content: Notification body
            - channel: EMAIL, SMS, or PUSH
            - status: PENDING, SENDING, SENT, or FAILED
            - createdAt: When it was created
            - sentAt: When it was sent (null if not sent yet)
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of notifications (may be empty)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        [
                          {
                            "id": 1,
                            "title": "Welcome!",
                            "content": "Hello...",
                            "channel": "EMAIL",
                            "status": "SENT",
                            "userEmail": "john.doe@example.com",
                            "createdAt": "2024-11-07T15:30:00",
                            "sentAt": "2024-11-07T15:30:05"
                          },
                          {
                            "id": 2,
                            "title": "Reminder",
                            "content": "Don't forget...",
                            "channel": "SMS",
                            "status": "PENDING",
                            "userEmail": "john.doe@example.com",
                            "createdAt": "2024-11-07T16:00:00"
                          }
                        ]
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            )
    })
    ResponseEntity<?> findAll(Authentication authentication);

    /**
     * Obtener una notificación específica por ID
     */
    @Operation(
            summary = "Get a notification by ID",
            description = """
            Returns a specific notification by its ID.
            
            Security: You can only access your own notifications.
            If the notification doesn't exist or belongs to another user, returns 404.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "title": "Welcome!",
                          "content": "Hello John, welcome!",
                          "channel": "EMAIL",
                          "status": "SENT",
                          "userEmail": "john.doe@example.com",
                          "createdAt": "2024-11-07T15:30:00",
                          "sentAt": "2024-11-07T15:30:05"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notification not found or you don't have permission",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "message": "Notification not found or you don't have permission to access it"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            )
    })
    ResponseEntity<?> findById(
            @PathVariable Long id,
            Authentication authentication
    );

    /**
     * Actualizar una notificación existente
     */
    @Operation(
            summary = "Update a notification",
            description = """
            Updates an existing notification's title and content.
            
            What can be updated:
            - title ✓
            - content ✓
            
            What CANNOT be updated:
            - channel (the notification may have already been sent)
            - status (managed by the system)
            - userEmail (cannot change ownership)
            
            Security: You can only update your own notifications.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "title": "Updated title",
                          "content": "Updated content",
                          "channel": "EMAIL",
                          "status": "SENT",
                          "userEmail": "john.doe@example.com",
                          "createdAt": "2024-11-07T15:30:00",
                          "sentAt": "2024-11-07T15:30:05"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "title": "Title is required"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notification not found or you don't have permission"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            )
    })
    ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationDto dto,
            Authentication authentication
    );

    /**
     * Eliminar una notificación
     */
    @Operation(
            summary = "Delete a notification",
            description = """
            Deletes a notification permanently (hard delete).
            
            Security: You can only delete your own notifications.
            
            Note: This is a physical deletion. The notification is removed from the database.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Notification deleted successfully (no content returned)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notification not found or you don't have permission",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "message": "Notification not found or you don't have permission to access it"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            )
    })
    ResponseEntity<?> delete(
            @PathVariable Long id,
            Authentication authentication
    );
}
