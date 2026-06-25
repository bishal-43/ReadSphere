package com.p99training.book_store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", "Validation failed",
                        "errors", errors
                ));
    }

    @ExceptionHandler(CsvException.class)
    public ResponseEntity<?> handleCsvException(
            CsvException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<?> handle(
            RuntimeException ex
    ){

        return ResponseEntity
                .status(
                        HttpStatus.NOT_FOUND
                )
                .body(
                        Map.of(
                                "status",
                                404,

                                "message",
                                ex.getMessage()
                        )
                );
    }

}