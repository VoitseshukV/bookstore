package com.bookstore.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bookstore.core.model.CartItem;
import com.bookstore.core.repository.cart.CartItemRepository;
import com.bookstore.core.util.TestDataFactory;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

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
                    new ClassPathResource("database/category/fill-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/fill-book-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/fill-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cart/fill-shopping-carts.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cart/fill-shopping-cart-items.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findCardItemByBookId: Find existing cart item")
    public void findCardItemByBookId_ExistingCartItem_ReturnsCartItem() {
        // Given
        CartItem expected = TestDataFactory.getCartItemTemplate(0);

        // When
        CartItem actual = cartItemRepository.findCardItemByBookId(2L, 1L);

        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "shoppingCart");
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getBook(), actual.getBook());
    }

    @Test
    @DisplayName("findCardItemByBookId: Find non-existing cart item")
    public void findCardItemByBookId_NonExistingCartItem_ReturnsNull() {
        // When
        CartItem actual = cartItemRepository.findCardItemByBookId(2L, 3L);

        // Then
        assertNull(actual);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/clear-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/clear-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/clear-book-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/clear-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cart/clear-shopping-carts.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cart/clear-shopping-cart-items.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
