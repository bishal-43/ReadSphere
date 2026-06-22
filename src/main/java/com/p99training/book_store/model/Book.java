package com.p99training.book_store.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // automatically generates standard boilerplate code for your data classes during compilation
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private Long id;

    private String bookName;

    private String authorName;

    private String category;

    private String publisher;

    private double price;

    private Integer quantity;

    private Integer publisherYear;

    private String isbn;

    private String language;


}
