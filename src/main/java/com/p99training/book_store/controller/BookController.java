package com.p99training.book_store.controller;

import com.p99training.book_store.model.Book;
import com.p99training.book_store.service.BookService;
import com.p99training.book_store.service.CsvLoaderService;
import com.p99training.book_store.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor                   // generates a constructor with required arguments
public class BookController {

    private final BookService bookService;      // Dependency Injection

    private final ReportService reportService;

    private final CsvLoaderService csvLoaderService;


    @GetMapping("/json")
    public ResponseEntity<List<Book>> getJson() {

        return ResponseEntity.ok(
                csvLoaderService.loadBooks()
        );
    }


    @GetMapping("/report")
    public ResponseEntity<String> getReport() {

        List<Book> books = csvLoaderService.loadBooks();

        return ResponseEntity.ok(reportService.generateReport(books));
    }


    @GetMapping
    public ResponseEntity<List<Book>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,


            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        return ResponseEntity.ok(
                bookService.getBooks(page,size,sortBy, direction)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(bookService.getById(id));
    }


    @PostMapping
    public ResponseEntity<Book> create(
            @Valid @RequestBody Book book
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(book));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable Long id,
            @Valid @RequestBody Book book
    ) {

        return ResponseEntity.ok(
                bookService.update(id, book));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(bookService.delete(id));
    }

}