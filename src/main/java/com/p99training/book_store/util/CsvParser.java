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
import java.util.Set;
import java.util.stream.Collectors;

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
        for (String[] row: rows){
            lineNum++;
            if (row.length < 10) {
                throw new CsvException("Invalid CSV row at line " + lineNum + ": expected 10 columns, but got " + row.length);
            }

            Book book = new Book();

            try {
                book.setId(Long.parseLong(row[0].trim()));
            } catch (NumberFormatException e) {
                throw new CsvException("Invalid ID format at line " + lineNum + ": " + row[0]);
            }

            book.setBookName(row[1] != null ? row[1].trim() : "");
            book.setAuthorName(row[2] != null ? row[2].trim() : "");
            book.setCategory(row[3] != null ? row[3].trim() : "");
            book.setPublisher(row[4] != null ? row[4].trim() : "");

            try {
                book.setPrice(Double.parseDouble(row[5].trim()));
            } catch (NumberFormatException e) {
                throw new CsvException("Invalid Price format at line " + lineNum + ": " + row[5]);
            }

            try {
                book.setQuantity(Integer.parseInt(row[6].trim()));
            } catch (NumberFormatException e) {
                throw new CsvException("Invalid Quantity format at line " + lineNum + ": " + row[6]);
            }

            try {
                book.setPublisherYear(Integer.parseInt(row[7].trim()));
            } catch (NumberFormatException e) {
                throw new CsvException("Invalid Published Year format at line " + lineNum + ": " + row[7]);
            }

            book.setIsbn(row[8] != null ? row[8].trim() : "");
            book.setLanguage(row[9] != null ? row[9].trim() : "");

            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            if (!violations.isEmpty()) {
                String validationErrors = violations.stream()
                        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                        .collect(Collectors.joining(", "));
                throw new CsvException("Validation failed at line " + lineNum + ": " + validationErrors);
            }

            books.add(book);
        }
        return books;
    }
}
