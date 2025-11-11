package takehomechallenge.dto.request;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private String email;
    private String message;

    public AuthResponseDto(){}

    // ctor for login
    public AuthResponseDto(String token, String email) {
        this.token = token;
        this.email = email;

    }

    //ctor with message for "register"
    public AuthResponseDto(String message) {
        this.message = message;
    }
}
