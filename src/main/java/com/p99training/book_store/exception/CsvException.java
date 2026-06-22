package com.p99training.book_store.exception;

public class CsvException extends RuntimeException{
    public CsvException(
            String message
    ) {
        super(message);
    }
}
