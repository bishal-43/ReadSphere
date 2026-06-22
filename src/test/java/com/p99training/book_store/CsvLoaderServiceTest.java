package com.p99training.book_store;

import com.p99training.book_store.model.Book;

import com.p99training.book_store.service.CsvLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CsvLoaderServiceTest {

    @Autowired
    private CsvLoaderService csvLoaderService;



    @Test
    void shouldLoadBooksSuccessfully() {

        // Act
        List<Book> books = csvLoaderService.loadBooks();

        // Assert
        assertNotNull(books);

        assertFalse(
                books.isEmpty()
        );
    }


    @Test
    void shouldReturnValidBookObjects() {

        List<Book> books =
                csvLoaderService.loadBooks();

        Book firstBook =
                books.get(0);

        assertNotNull(
                firstBook.getId()
        );

        assertNotNull(
                firstBook.getBookName()
        );
    }

}