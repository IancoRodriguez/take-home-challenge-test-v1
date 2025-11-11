package takehomechallenge.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import takehomechallenge.config.IAuthApi;
import takehomechallenge.dto.UserDto;
import takehomechallenge.dto.request.AuthResponseDto;
import takehomechallenge.dto.request.LoginDto;
import takehomechallenge.dto.request.RegisterDto;
import takehomechallenge.service.IAuthService;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration, authentication and account management operations")
public class AuthController implements IAuthApi {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        UserDto user = authService.register(email, password);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponseDto("User registered successfully!"));

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        // Authenticate and generate token
        String token = authService.login(email, password);

        return ResponseEntity.ok(new AuthResponseDto(token, loginDto.getEmail()));
    }
}
