package com.bookstore.core.controller;

import static com.bookstore.core.util.TestDataFactory.getBookDtoTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCreateBookRequestDtoTemplateById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getAllBooks: Return available books")
    @WithMockUser(username = "user")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllBooks_BooksAvailable_ReturnsExpectedBooks() throws Exception {
        // Given
        BookDto bookDto1 = getBookDtoTemplateById(0);
        BookDto bookDto2 = getBookDtoTemplateById(1);
        BookDto bookDto3 = getBookDtoTemplateById(2);
        List<BookDto> expected = List.of(bookDto1, bookDto2, bookDto3);

        // When
        MvcResult mvcResult = mockMvc.perform(
                get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>(){}
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,
                "description,coverImage,categoryIds");
    }

    @Test
    @DisplayName("createBook: Add new valid book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBook_ValidBook_ReturnsCreatedBookDto() throws Exception {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(0);
        BookDto expected = getBookDtoTemplateById(0);
        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("createBook: The book already exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBook_BookAlreadyExists_Exception() throws Exception {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(2);
        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When
        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(
                        post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()));
        String actual = exception.getMessage();

        // Then
        assertTrue(actual.contains("Duplicate entry"));
    }

    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("getBookById: Return book by valid ID")
    @WithMockUser(username = "user")
    public void getBookById_ValidId_ReturnsFoundBook() throws Exception {
        // Given
        BookDto expected = getBookDtoTemplateById(0);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,
                "description,coverImage,categoryIds");
    }

    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    @DisplayName("getBookById: Get non-existing book")
    @WithMockUser(username = "user")
    public void getBookById_NonExistingBook_Exception() throws Exception {
        // Given
        String expected = "Can't get book with id: 1";

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/books/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("updateBookById: Update existing book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBookById_ValidBook_ReturnsUpdatedBookDto() throws Exception {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(2);
        BookDto expected = getBookDtoTemplateById(2);
        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/books/3")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateBookById: Update non-existing book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBookById_NonExistingBook_Exception() throws Exception {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(2);
        String jsonRequest = objectMapper.writeValueAsString(bookDto);
        String expected = "Can't get book with id: 3";

        // When
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/books/3")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("deleteBookById: Delete book by valid ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteBookById_ValidId_DeleteBook() throws Exception {
        // When and then
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/books/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("searchBooks: Search books by name")
    @WithMockUser(username = "user")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/books/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchBooks_SearchByName_ReturnsExpectedBooks() throws Exception {
        // Given
        BookDto bookDto = getBookDtoTemplateById(1);
        List<BookDto> expected = List.of(bookDto);
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto(
                "American",
                "",
                "",
                ""
        );

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/books/search"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>(){}
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,
                "description,coverImage,categoryIds");
    }
}
