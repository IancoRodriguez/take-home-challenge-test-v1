package takehomechallenge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración global de OpenAPI/Swagger
 *
 * Esta clase habilita:
 * - El botón "Authorize" en Swagger UI
 * - El esquema de seguridad JWT
 * - Información general de la API
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Notifications API",
                version = "1.0.0",
                description = """
            RESTful API for user authentication and notification management.
            
            Features:
            - JWT-based authentication
            - Multi-channel notifications (EMAIL, SMS, PUSH)
            - Asynchronous notification sending
            - Strategy Pattern for extensibility
            
            Security:
            All /api/* endpoints require JWT authentication.
            Use the Authorize button to set your JWT token.
            """,
                contact = @Contact(
                        name = "Your Name",
                        email = "your.email@example.com"
                )
        ),
        servers = {
                @Server(
                        description = "Development Server",
                        url = "http://localhost:8080"
                )
        },
        // Esto habilita el botón "Authorize" en Swagger UI
        security = @SecurityRequirement(name = "bearerAuth")
)
// Este @SecurityScheme define CÓMO funciona la autenticación JWT
@SecurityScheme(
        name = "bearerAuth", // Nombre que usamos en @SecurityRequirement
        description = "JWT authentication. Get your token from /auth/login and paste it here (without 'Bearer' prefix)",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // No necesita código, solo las anotaciones
}
