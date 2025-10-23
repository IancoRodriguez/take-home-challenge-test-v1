package takehomechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import takehomechallenge.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404",ex.getMessage()));
    }

    //Manejar cualquier otra excepcion no controlada

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500",ex.getMessage()));
    }
}
