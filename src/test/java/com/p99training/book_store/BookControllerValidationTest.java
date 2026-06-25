package com.p99training.book_store;

import com.p99training.book_store.exception.CsvException;
import com.p99training.book_store.model.Book;
import com.p99training.book_store.util.CsvParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookControllerValidationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldCreateBookSuccessfullyWhenPayloadIsValid() throws Exception {
        String validBookJson = "{"
                + "\"id\":999,"
                + "\"bookName\":\"Valid Book Name\","
                + "\"authorName\":\"Valid Author\","
                + "\"category\":\"Programming\","
                + "\"publisher\":\"Valid Publisher\","
                + "\"price\":99.99,"
                + "\"quantity\":10,"
                + "\"publisherYear\":2025,"
                + "\"isbn\":\"9780132350884\","
                + "\"language\":\"English\""
                + "}";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(999))
                .andExpect(jsonPath("$.bookName").value("Valid Book Name"));
    }

    @Test
    void shouldRejectBookCreationWhenPayloadIsInvalid() throws Exception {
        // Book with invalid attributes:
        // - id is negative
        // - bookName is blank
        // - price is negative
        // - isbn is invalid
        // - publisherYear is in future (2030)
        String invalidBookJson = "{"
                + "\"id\":-1,"
                + "\"bookName\":\"   \","
                + "\"authorName\":\"Valid Author\","
                + "\"category\":\"Programming\","
                + "\"publisher\":\"Valid Publisher\","
                + "\"price\":-5.0,"
                + "\"quantity\":-2,"
                + "\"publisherYear\":2030,"
                + "\"isbn\":\"12345\","
                + "\"language\":\"English\""
                + "}";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.id").exists())
                .andExpect(jsonPath("$.errors.bookName").exists())
                .andExpect(jsonPath("$.errors.price").exists())
                .andExpect(jsonPath("$.errors.quantity").exists())
                .andExpect(jsonPath("$.errors.publisherYear").exists())
                .andExpect(jsonPath("$.errors.isbn").exists());
    }

    @Test
    void shouldRejectBookUpdateWhenPayloadIsInvalid() throws Exception {
        String invalidBookJson = "{"
                + "\"id\":1,"
                + "\"bookName\":\"Valid Name\","
                + "\"authorName\":\"\"," // Blank authorName
                + "\"category\":\"Programming\","
                + "\"publisher\":\"Valid Publisher\","
                + "\"price\":10.0,"
                + "\"quantity\":5,"
                + "\"publisherYear\":2020,"
                + "\"isbn\":\"9780132350884\","
                + "\"language\":\"English\""
                + "}";

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.authorName").exists());
    }

    @Test
    void shouldParseValidCsvSuccessfully() throws Exception {
        String csvData = "id,bookName,authorName,category,publisher,price,quantity,publishedYear,isbn,language\n" +
                "101,Book One,Author One,Tech,Pub One,150.00,5,2022,9780132350884,English\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        List<Book> parsedBooks = CsvParser.parse(inputStream);

        assertNotNull(parsedBooks);
        assertEquals(1, parsedBooks.size());
        Book book = parsedBooks.get(0);
        assertEquals(101L, book.getId());
        assertEquals("Book One", book.getBookName());
        assertEquals(150.00, book.getPrice());
        assertEquals(5, book.getQuantity());
        assertEquals(2022, book.getPublisherYear());
    }

    @Test
    void shouldThrowCsvExceptionWhenCsvHasInvalidColumnCounts() {
        String csvData = "id,bookName,authorName,category,publisher,price,quantity,publishedYear,isbn,language\n" +
                "101,Book One,Author One,Tech,Pub One,150.00\n"; // Missing columns

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        CsvException exception = assertThrows(CsvException.class, () -> CsvParser.parse(inputStream));
        assertTrue(exception.getMessage().contains("expected 10 columns"));
    }

    @Test
    void shouldThrowCsvExceptionWhenCsvHasNumberFormattingError() {
        String csvData = "id,bookName,authorName,category,publisher,price,quantity,publishedYear,isbn,language\n" +
                "101,Book One,Author One,Tech,Pub One,invalid_price,5,2022,9780132350884,English\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        CsvException exception = assertThrows(CsvException.class, () -> CsvParser.parse(inputStream));
        assertTrue(exception.getMessage().contains("Invalid Price format"));
    }

    @Test
    void shouldThrowCsvExceptionWhenCsvRowFailsBeanValidation() {
        String csvData = "id,bookName,authorName,category,publisher,price,quantity,publishedYear,isbn,language\n" +
                "101, ,Author One,Tech,Pub One,150.00,5,2022,9780132350884,English\n"; // Blank bookName

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        CsvException exception = assertThrows(CsvException.class, () -> CsvParser.parse(inputStream));
        assertTrue(exception.getMessage().contains("Validation failed"));
        assertTrue(exception.getMessage().contains("bookName"));
    }
}
