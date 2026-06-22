package com.p99training.book_store.util;

import com.opencsv.CSVReader;
import com.p99training.book_store.model.Book;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static List<Book> parse(InputStream inputStream)
        throws Exception {

        List<Book> books = new ArrayList<>();


        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

        List<String[]> rows = reader.readAll();

        if(rows == null || rows.isEmpty()){
            return books;
        }

        rows.remove(0);

        for (String[] row: rows){
            Book book = new Book();

            book.setId(Long.parseLong(row[0]));

            book.setBookName(row[1]);

            book.setAuthorName(row[2]);

            book.setCategory(row[3]);

            book.setPublisher(row[4]);

            book.setPrice(Double.parseDouble(row[5]));

            book.setQuantity(Integer.parseInt(row[6]));

            book.setPublisherYear(Integer.parseInt(row[7]));

            book.setIsbn(row[8]);

            book.setLanguage(row[9]);

            books.add(book);
        }
        return books;
    }
}
