package com.p99training.book_store.exception;

import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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