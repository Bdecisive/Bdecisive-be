package edu.ilstu.bdecisive.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ServiceException extends Exception {
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    // Constructor with message, cause, error code, and status
    public ServiceException(String message, Throwable cause, String errorCode, HttpStatus status) {
        super(message, cause);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with only message and status
    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
