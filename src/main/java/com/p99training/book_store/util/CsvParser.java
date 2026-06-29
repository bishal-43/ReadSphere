package com.p99training.book_store.util;

import com.opencsv.CSVReader;
import com.p99training.book_store.model.Book;
import com.p99training.book_store.exception.CsvException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




public class CsvParser {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static List<Book> parse(InputStream inputStream)
        throws Exception {

        List<Book> books = new ArrayList<>();

        List<String[]> rows;
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            rows = reader.readAll();
        } catch (Exception e) {
            throw new CsvException("Failed to read CSV input stream: " + e.getMessage());
        }

        if (rows == null || rows.isEmpty()){
            return books;
        }

        rows.remove(0);

        int lineNum = 1; // Line 1 is the header
        for (String[] row: rows) {
            lineNum++;
            if (row.length < 10) {
                throw new CsvException("Invalid CSV row at line " + lineNum + ": expected 10 columns, but got " + row.length);
            }

            Book book;

            try {
                book = Book.builder()
                        .id(Long.parseLong(row[0].trim()))
                        .bookName(row[1] != null ? row[1].trim() : "")
                        .authorName(row[2] != null ? row[2].trim() : "")
                        .category(row[3] != null ? row[3].trim() : "")
                        .publisher(row[4] != null ? row[4].trim() : "")
                        .price(Double.parseDouble(row[5].trim()))
                        .quantity(Integer.parseInt(row[6].trim()))
                        .publisherYear(Integer.parseInt(row[7].trim()))
                        .isbn(row[8] != null ? row[8].trim() : "")
                        .language(row[9] != null ? row[9].trim() : "")
                        .build();
            } catch (NumberFormatException e) {

                throw new CsvException("Invalid data at line " + lineNum);
            }
            books.add(book);
        }
        return books;
    }
}
