package com.p99training.book_store.service;


import com.p99training.book_store.model.Book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {



    private final List<Book> books;

    public BookService(
            CsvLoaderService loader
    ) {
        this.books =
                loader.loadBooks();
    }





    public List<Book> getAll() {
        log.info("Fetching all books");
        return books;
    }

    public Book getById(Long id) {

        log.info("Finding book {}", id);

        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Book not found: " + id));
    }

    public Book create(Book book) {

        books.add(book);

        log.info("Book created");

        return book;
    }

    public Book update(Long id, Book updatedBook) {

        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getId().equals(id)) {

                books.set(i, updatedBook);

                log.info(
                        "Book updated {}",
                        id
                );

                return updatedBook;
            }
        }

        log.warn(
                "Book not found {}",
                id
        );

        throw new RuntimeException(
                "Book not found: " + id
        );
    }

    public String delete(Long id) {

        boolean removed = books.removeIf(
                        book -> book.getId().equals(id));

        if (removed) {

            log.info("Deleted {}", id );

            return "Deleted";
        }

        log.warn("Delete failed {}", id );

        return "Not found";
    }
}