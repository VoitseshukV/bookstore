package com.bookstore.core.repository;

import static com.bookstore.core.util.TestDataFactory.getBookTemplateById;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookstore.core.model.Book;
import com.bookstore.core.repository.book.BookRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/fill-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/fill-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/fill-book-categories.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Find one book by category Sci-Fi")
    public void testFindAllByCategoryId_CategorySciFi_ReturnsOneBook() {
        // Given
        List<Book> expectedSciFi = new ArrayList<>();
        expectedSciFi.add(getBookTemplateById(2));

        // When
        List<Book> actualSciFi = bookRepository.findAllByCategoryId(1L);

        // Then
        assertEquals(1, actualSciFi.size());
        EqualsBuilder.reflectionEquals(expectedSciFi, actualSciFi, "categories");
    }

    @Test
    @DisplayName("Find three books by category Fantasy")
    public void testFindAllByCategoryId_CategoryFantasy_ReturnsThreeBooks() {
        // Given
        List<Book> expectedFantasy = new ArrayList<>();
        expectedFantasy.add(getBookTemplateById(0));
        expectedFantasy.add(getBookTemplateById(1));
        expectedFantasy.add(getBookTemplateById(2));

        // When
        List<Book> actualFantasy = bookRepository.findAllByCategoryId(2L);

        // Then
        assertEquals(3, actualFantasy.size());
        EqualsBuilder.reflectionEquals(expectedFantasy, actualFantasy, "categories");
    }

    @Test
    @DisplayName("Find any book by a non-existent category")
    public void testFindAllByCategoryId_ExpectedCategory_ReturnsEmptyList() {
        // When
        List<Book> actual = bookRepository.findAllByCategoryId(0L);

        // Then
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Find any book by null category ID")
    public void testFindAllByCategoryId_NullCategoryId_ReturnsEmptyList() {
        // When
        List<Book> actual = bookRepository.findAllByCategoryId(null);

        // Then
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Find any book by negative category ID")
    public void testFindAllByCategoryId_NegativeId_ReturnsEmptyList() {
        // When
        List<Book> actual = bookRepository.findAllByCategoryId(-1L);

        // Then
        assertEquals(0, actual.size());
    }

    @AfterAll
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/books/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    static void afterAll(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/clear-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/clear-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/clear-book-categories.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
