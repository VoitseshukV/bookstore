package com.bookstore.core.controller;

import static com.bookstore.core.util.TestDataFactory.getOrderDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getOrderItemDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUpdateOrderDtoTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.dto.UpdateOrderDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getOrders: Get current user's orders")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrders_ValidUser_ReturnsOrderDtos() throws Exception {
        // Given
        OrderDto orderDto1 = getOrderDtoTemplate(0);
        OrderDto orderDto2 = getOrderDtoTemplate(1);
        List<OrderDto> expected = List.of(orderDto1, orderDto2);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<OrderDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addOrder: Add new order by shopping cart")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/cart/fill-shopping-cart-items.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addOrder_ValidData_ReturnsOrderDto() throws Exception {
        // Given
        OrderDto expected = getOrderDtoTemplate(2);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/api/orders"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        OrderDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,
                "id, orderDate, orderItems.id");
    }

    @Test
    @DisplayName("addOrder: Add new order by empty shopping cart")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/cart/fill-shopping-carts.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/cart/clear-shopping-cart-items.sql",
            "classpath:database/cart/clear-shopping-carts.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addOrder_EmptyShoppingCart_Exception() throws Exception {
        // Given
        String expected = "Shopping cart is empty";

        // When
        MvcResult mvcResult = mockMvc.perform(post("/api/orders"))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("getOrderById: Get order by valid ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderById_ValidId_ReturnsOrderDto() throws Exception {
        // Given
        OrderDto expected = getOrderDtoTemplate(0);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        OrderDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderById: Get order by non-existing ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderById_NonExistingId_Exception() throws Exception {
        // Given
        long id = 4L;
        String expected = "Can't find order with ID: " + id;

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/" + id))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("getOrderItems: Get order items by ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderItems_ValidId_ReturnsOrderItemDtos() throws Exception {
        // Given
        OrderItemDto orderItemDto1 = getOrderItemDtoTemplate(0);
        OrderItemDto orderItemDto2 = getOrderItemDtoTemplate(1);
        OrderItemDto orderItemDto3 = getOrderItemDtoTemplate(2);
        List<OrderItemDto> expected = List.of(orderItemDto1, orderItemDto2, orderItemDto3);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/1/items"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<OrderItemDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItems: Get order items by non-existing ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderItems_NonExistingId_Exception() throws Exception {
        // Given
        long id = 4L;
        String expected = "Can't find order with ID: " + id;

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/" + id + "/items"))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("getOrderItemById: Get order item by order and item IDs")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderItemById_ValidIds_ReturnsOrderItemDto() throws Exception {
        // Given
        OrderItemDto expected = getOrderItemDtoTemplate(0);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/1/items/1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        OrderItemDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderItemDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItemById: Get order item by non-existing ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderItemById_NonExistingId_Exception() throws Exception {
        // Given
        long id = 4L;
        String expected = "Can't find order item with ID: " + id;

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/orders/1/items/" + id))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("updateOrderById: Update order by valid ID")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateOrderById_ValidId_ReturnsOrderDto() throws Exception {
        // Given
        UpdateOrderDto requestDto = getUpdateOrderDtoTemplate(0);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        OrderDto expected = getOrderDtoTemplate(0);

        // When
        MvcResult mvcResult = mockMvc.perform(patch("/api/orders/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        OrderDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateOrderById: Update order items by non-existing ID")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql",
            "classpath:database/user/fill-users.sql",
            "classpath:database/order/fill-orders.sql",
            "classpath:database/order/fill-order-items.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql",
            "classpath:database/user/clear-users.sql",
            "classpath:database/order/clear-order-items.sql",
            "classpath:database/order/clear-orders.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateOrderById_NonExistingId_Exception() throws Exception {
        // Given
        UpdateOrderDto requestDto = getUpdateOrderDtoTemplate(0);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        long id = 4L;
        String expected = "Can't find order with ID: " + id;

        // When
        MvcResult mvcResult = mockMvc.perform(patch("/api/orders/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }
}
