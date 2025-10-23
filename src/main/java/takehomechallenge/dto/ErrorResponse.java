package takehomechallenge.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(String number, String message) {
        this.code = number;
        this.message = message;
    }
}
