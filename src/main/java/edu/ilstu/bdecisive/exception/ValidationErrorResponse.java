package edu.ilstu.bdecisive.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(String message, HttpStatus status, LocalDateTime timestamp, Map<String, String> errors) {
        super(message, status, timestamp);
        this.errors = errors;
    }
}
