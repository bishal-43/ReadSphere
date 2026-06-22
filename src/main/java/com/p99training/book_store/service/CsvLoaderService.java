package com.p99training.book_store.service;

import com.p99training.book_store.model.Book;
import com.p99training.book_store.util.CsvParser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CsvLoaderService {

    @Value("${book.csv.file}")
    private String file;

    private List<Book> books;

    public List<Book> loadBooks() {

        try {
            log.info("Loading csv...");

            ClassPathResource resource = new ClassPathResource(file);

            books = CsvParser.parse(resource.getInputStream());

            log.info("Loaded {} books", books.size());

        } catch (Exception e) {

            log.error("Error loading csv: {}", e.getMessage());

            throw new RuntimeException(
                    "Failed to load csv", e
            );
        }

        return books;
    }

}
