package com.p99training.book_store.controller;

import com.p99training.book_store.model.Book;
import com.p99training.book_store.service.CsvLoaderService;
import com.p99training.book_store.service.BookService;
import com.p99training.book_store.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService csvService;

    private final ReportService reportService;

    private final CsvLoaderService csvLoaderService;

    @GetMapping("/json")
    public ResponseEntity<List<Book>> getJson() {


        return ResponseEntity.ok(
                csvLoaderService.loadBooks()
        );
    }

    @GetMapping("/report")
    public String getReport(){
        List<Book> books = csvLoaderService.loadBooks();

        return reportService.generateReport(books);
    }

    @GetMapping()
    public List<Book> getAll(){
        return csvService.getAll();
    }


    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id){
        return csvService.getById(id);
    }



    @PostMapping()
    public Book create(@RequestBody Book book){
        return csvService.create(book);
    }


    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book){
        return csvService.update(id,book);
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        return csvService.delete(id);
    }


}