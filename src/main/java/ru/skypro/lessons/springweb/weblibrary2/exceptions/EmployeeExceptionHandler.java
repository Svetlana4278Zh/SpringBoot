package ru.skypro.lessons.springweb.weblibrary2.exceptions;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
public class EmployeeExceptionHandler {

    Logger logger = LoggerFactory.getLogger(EmployeeExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<?> handleIOException(IOException ioException) {
        logger.error(ioException.getMessage(), ioException);
        return new ResponseEntity<>(ioException.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> internalServerError() {
        return ResponseEntity.internalServerError().build();
    }
    @ExceptionHandler({ReportNotFoundException.class, EmployeeNotFoundException.class})
    public ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }
}
