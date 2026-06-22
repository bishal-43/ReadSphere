package com.p99training.book_store.service;

import com.p99training.book_store.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.FileWriter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReportService {
    public String generateReport(List<Book> books){
        try{
            String fileName = "report.txt";

            if (
                    books == null || books.isEmpty()
            ) {

                FileWriter writer = new FileWriter(fileName);

                writer.write(
                        "BOOK REPORT\n"
                );

                writer.write(
                        "====================\n\n"
                );

                writer.write(
                        "No book records found."
                );

                writer.close();

                return "Empty report generated";
            }

            FileWriter writer = new FileWriter(fileName);

            Book maxBook = books.stream()
                    .max(
                            (a,b) -> Double.compare(a.getPrice(),b.getPrice())
                    )
                    .orElse(null);


            Book minBook = books.stream()
                    .min(
                            (a,b) -> Double.compare(a.getPrice(),b.getPrice())
                    )
                    .orElse(null);

            double maxPrice = books.stream()
                    .mapToDouble(Book::getPrice)
                    .max()
                    .orElse(0);


            double minPrice = books.stream()
                    .mapToDouble(Book::getPrice)
                    .min()
                    .orElse(0);

            double avgPrice = books.stream()
                    .mapToDouble(Book::getPrice)
                    .average()
                    .orElse(0);

            int totalQuantity = books.stream()
                    .mapToInt(Book::getQuantity)
                    .sum();

            double inventoryValue = books.stream()
                    .mapToDouble(b -> b.getPrice() * b.getQuantity())
                    .sum();

            int latestYear = books.stream()
                    .mapToInt(Book::getPublisherYear)
                    .max()
                    .orElse(0);

            int oldestYear = books.stream()
                    .mapToInt(Book::getPublisherYear)
                    .min()
                    .orElse(0);

            Map<String, Long> categoryCount = books.stream()
                            .collect(Collectors.groupingBy(
                                    Book::getCategory,
                                    Collectors.counting()
                            ));

            Map<String, Long> languageCount = books.stream()
                            .collect(Collectors.groupingBy(
                                    Book::getLanguage,
                                    Collectors.counting()
                            ));


            Map<String, Long> publisherCount = books.stream()
                            .collect(Collectors.groupingBy(
                                    Book::getPublisher,
                                    Collectors.counting()
                            ));


            writer.write("BOOK REPORT\n");
            writer.write("====================\n\n");

            writer.write(
                    "total books: " + books.size() + "\n"
            );

            writer.write(
                    "BOOKS PER CATEGORY\n"
            );

            writer.write(
                    "----------------------\n"
            );

            for (
                    Map.Entry<String, Long>
                            entry
                    : categoryCount.entrySet()
            ) {

                writer.write(
                        entry.getKey()
                                + " : "
                                + entry.getValue()
                                + "\n"
                );
            }

            writer.write("\n");

            writer.write("Max price: " + maxPrice + "\n");

            writer.write("Min price: " + minPrice + "\n");

            writer.write("Min price: " + minPrice + "\n");


            writer.write("Most expensive book: " + maxBook.getBookName() + "\n");

            writer.write("Least expensive book: " + minBook.getBookName() + "\n");

            writer.write("Average price: " + avgPrice + "\n");

            writer.write("Total quantity: " + totalQuantity + "\n" );

            writer.write("Total Inventory Value" + inventoryValue + "\n");

            writer.write("Latest published year: " + latestYear + "\n");

            writer.write("Oldest published year: " + oldestYear + "\n");

            writer.write(
                    "\nLANGUAGE DISTRIBUTION\n"
            );

            writer.write(
                    "----------------------\n"
            );

            for (
                    Map.Entry<String, Long>
                            entry
                    : languageCount.entrySet()
            ) {

                writer.write(
                        entry.getKey()
                                + " : "
                                + entry.getValue()
                                + "\n"
                );
            }

            writer.write(
                    "\nPUBLISHER-WISE COUNT\n"
            );

            writer.write(
                    "----------------------\n"
            );

            for (
                    Map.Entry<String, Long>
                            entry
                    : publisherCount.entrySet()
            ) {

                writer.write(
                        entry.getKey()
                                + " : "
                                + entry.getValue()
                                + "\n"
                );
            }

            writer.close();

            return "report.txt generated";



        }catch(Exception e){
            throw new RuntimeException("Report generation failed");
        }
    }
}
