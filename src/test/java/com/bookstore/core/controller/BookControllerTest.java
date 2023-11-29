package com.bookstore.core.controller;

import static org.junit.jupiter.api.Assertions.*;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
        BookDto bookDto1 = new BookDto()
                .setId(1L)
                .setTitle("Anansi Boys")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780060515195")
                .setPrice(BigDecimal.valueOf(500));
        BookDto bookDto2 = new BookDto()
                .setId(2L)
                .setTitle("American Gods")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780062896261")
                .setPrice(BigDecimal.valueOf(450));
        BookDto bookDto3 = new BookDto()
                .setId(3L)
                .setTitle("Elantris")
                .setAuthor("Brandon Sanderson")
                .setIsbn("9780765350374")
                .setPrice(BigDecimal.valueOf(420));
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
        CreateBookRequestDto bookDto = new CreateBookRequestDto(
                "Elantris",
                "Brandon Sanderson",
                "9780765350374",
                BigDecimal.valueOf(450),
                "",
                "",
                Set.of());
        BookDto expected = new BookDto()
                .setTitle(bookDto.title())
                .setAuthor(bookDto.author())
                .setIsbn(bookDto.isbn())
                .setPrice(bookDto.price())
                .setDescription(bookDto.description())
                .setCoverImage(bookDto.coverImage())
                .setCategoryIds(Set.of());
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
        CreateBookRequestDto bookDto = new CreateBookRequestDto(
                "Elantris",
                "Brandon Sanderson",
                "9780765350374",
                BigDecimal.valueOf(450),
                "",
                "",
                Set.of());
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
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Anansi Boys")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780060515195")
                .setPrice(BigDecimal.valueOf(500));

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
        CreateBookRequestDto bookDto = new CreateBookRequestDto(
                "Elantris",
                "Brandon Sanderson",
                "9780765350374",
                BigDecimal.valueOf(500),
                "",
                "",
                Set.of(1L, 2L));
        BookDto expected = new BookDto()
                .setId(3L)
                .setTitle(bookDto.title())
                .setAuthor(bookDto.author())
                .setIsbn(bookDto.isbn())
                .setPrice(bookDto.price())
                .setDescription(bookDto.description())
                .setCoverImage(bookDto.coverImage())
                .setCategoryIds(bookDto.categoryIds());
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
        CreateBookRequestDto bookDto = new CreateBookRequestDto(
                "Elantris",
                "Brandon Sanderson",
                "9780765350374",
                BigDecimal.valueOf(500),
                "",
                "",
                Set.of(1L, 2L));
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
        // Given

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
        BookDto bookDto = new BookDto()
                .setId(2L)
                .setTitle("American Gods")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780062896261")
                .setPrice(BigDecimal.valueOf(450));
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
