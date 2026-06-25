package com.p99training.book_store.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // automatically generates standard boilerplate code for your data classes during compilation
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Book {


    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be a positive number")
    private Long id;

    @NotBlank(message = "Book name cannot be blank")
    private String bookName;

    @NotBlank(message = "Author name cannot be blank")
    private String authorName;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "Publisher cannot be blank")
    private String publisher;

    @Positive(message = "Price must be a positive number")
    private double price;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Publisher year cannot be null")
    @Min(value = 1800, message = "Publisher year must be after 1800")
    @Max(value = 2026, message = "Publisher year cannot be in the future")
    private Integer publisherYear;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "^\\d{10}|\\d{13}$", message = "Invalid ISBN format. Must be 10 or 13 digits.")
    private String isbn;

    @NotBlank(message = "Language cannot be blank")
    private String language;


}
