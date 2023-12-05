package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getOrderItemDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getOrderItemTemplate;
import static com.bookstore.core.util.TestDataFactory.getOrderTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.OrderItemMapper;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.OrderItem;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.order.OrderItemRepository;
import com.bookstore.core.service.impl.OrderItemServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @Mock
    private OrderItemMapper orderItemMapper;
    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    @DisplayName("getOrderItems: Get order items by valid user email and order ID")
    public void getOrderItems_ValidEmailAndOrderId_ReturnsOrderItems() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        Order order = getOrderTemplate(0);
        OrderItem orderItem1 = getOrderItemTemplate(0);
        OrderItem orderItem2 = getOrderItemTemplate(1);
        OrderItem orderItem3 = getOrderItemTemplate(2);
        OrderItemDto orderItemDto1 = getOrderItemDtoTemplate(0);
        OrderItemDto orderItemDto2 = getOrderItemDtoTemplate(1);
        OrderItemDto orderItemDto3 = getOrderItemDtoTemplate(2);
        List<OrderItemDto> expected = List.of(orderItemDto1, orderItemDto2, orderItemDto3);
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderService.findByIdAndCheckByUser(user, 1L)).thenReturn(order);
        when(orderItemMapper.toDto(orderItem1)).thenReturn(orderItemDto1);
        when(orderItemMapper.toDto(orderItem2)).thenReturn(orderItemDto2);
        when(orderItemMapper.toDto(orderItem3)).thenReturn(orderItemDto3);

        // When
        List<OrderItemDto> actual = orderItemService.getOrderItems(email, 1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItems: Get order items by invalid user")
    public void getOrderItems_InvalidUser_Exception() {
        // Given
        String email = "user2@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderItemService.getOrderItems(email, 1L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItems: Get order items by invalid order ID")
    public void getOrderItems_InvalidOrderId_Exception() {
        // Given
        Long id = 4L;
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        String expected = "Can't find order with ID: " + id;
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderService.findByIdAndCheckByUser(user, id))
                .thenThrow(new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderItemService.getOrderItems(email, id));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItemById: Get order items by valid user email, order & item IDs")
    public void getOrderItemById_ValidArguments_ReturnsOrderItem() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        Order order = getOrderTemplate(0);
        OrderItem orderItem = getOrderItemTemplate(0);
        OrderItemDto expected = getOrderItemDtoTemplate(0);
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderService.findByIdAndCheckByUser(user, 1L)).thenReturn(order);
        when(orderItemRepository.findFirstByOrderAndId(order, 1L))
                .thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(expected);

        // When
        OrderItemDto actual = orderItemService.getOrderItemById(email, 1L, 1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItemById: Get order item by invalid user")
    public void getOrderItemById_InvalidUser_Exception() {
        // Given
        String email = "user2@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderItemService.getOrderItemById(email, 1L, 1L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItemById: Get order item by invalid order ID")
    public void getOrderItemById_InvalidOrderId_Exception() {
        // Given
        Long id = 4L;
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        String expected = "Can't find order with ID: " + id;
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderService.findByIdAndCheckByUser(user, id))
                .thenThrow(new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderItemService.getOrderItemById(email, id, 1L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderItemById: Get order item by invalid item ID")
    public void getOrderItemById_InvalidOrderItemId_Exception() {
        // Given
        Long id = 8L;
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        Order order = getOrderTemplate(0);
        String expected = "Can't find order item with ID: " + id;
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderService.findByIdAndCheckByUser(user, 1L)).thenReturn(order);
        when(orderItemRepository.findFirstByOrderAndId(order, id))
                .thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderItemService.getOrderItemById(email, 1L, id));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }
}
