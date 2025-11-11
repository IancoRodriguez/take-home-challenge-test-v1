package takehomechallenge.config;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import takehomechallenge.dto.request.AuthResponseDto;
import takehomechallenge.dto.request.LoginDto;
import takehomechallenge.dto.request.RegisterDto;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public interface IAuthApi {

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with email and password. Password is encrypted using BCrypt."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"User registered successfully!\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Email exists",
                                            value = "{\"error\": \"El email ya está registrado\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Validation error",
                                            value = "{\"email\": \"Email debe ser válido\"}"
                                    )
                            }
                    )
            )
    })
    ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto);

    @Operation(
            summary = "Login and get JWT token",
            description = "Authenticates user and returns JWT token valid for 24 hours"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{\"token\": \"eyJhbGc...\", \"email\": \"user@example.com\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Email o password incorrectos\"}"
                            )
                    )
            )
    })
    ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto);
}