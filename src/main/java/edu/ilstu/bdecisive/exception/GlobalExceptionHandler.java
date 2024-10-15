package edu.ilstu.bdecisive.exception;

import edu.ilstu.bdecisive.utils.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getStatus(),
                ex.getTimestamp()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
