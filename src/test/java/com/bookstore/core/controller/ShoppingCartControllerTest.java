package com.bookstore.core.controller;

import static com.bookstore.core.util.TestDataFactory.getAddCartItemRequestDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getCartItemDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getShoppingCartDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUpdateCartItemRequestDtoTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.core.dto.AddCartItemRequestDto;
import com.bookstore.core.dto.CartItemDto;
import com.bookstore.core.dto.ShoppingCartDto;
import com.bookstore.core.dto.UpdateCartItemRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getShoppingCart: Get shopping cart by current user")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCart_ValidUser_ReturnsShoppingCart() throws Exception {
        // Given
        ShoppingCartDto expected = getShoppingCartDtoTemplate(1);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addCartItem: Add new cart item")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addCartItem_ValidData_ReturnsCartItemDto() throws Exception {
        // Given
        AddCartItemRequestDto requestDto = getAddCartItemRequestDtoTemplate(0);
        CartItemDto expected = getCartItemDtoTemplate(0);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CartItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CartItemDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("updateCartItemById: Update cart item by ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItemById_ValidData_ReturnsCartItemDto() throws Exception {
        // Given
        UpdateCartItemRequestDto requestDto = getUpdateCartItemRequestDtoTemplate(0);
        CartItemDto expected = getCartItemDtoTemplate(0);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(put("/api/cart/items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CartItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CartItemDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("updateCartItemById: Update cart item by non-existing ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItemById_NonExistId_Exception() throws Exception {
        // Given
        long id = 4L;
        UpdateCartItemRequestDto requestDto = getUpdateCartItemRequestDtoTemplate(0);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Can't find cart entity with id " + id;

        // When
        MvcResult mvcResult = mockMvc.perform(put("/api/cart/items/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("deleteCartItemById: Delete cart item by ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCartItemById_ValidId_CartItemDeleted() throws Exception {
        // When and then
        mockMvc.perform(delete("/api/cart/items/1"))
                .andExpect(status().isNoContent());
    }
}
