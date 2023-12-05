package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getOrderDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getOrderTemplate;
import static com.bookstore.core.util.TestDataFactory.getShoppingCartTemplate;
import static com.bookstore.core.util.TestDataFactory.getUpdateOrderDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.UpdateOrderDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.exception.OrderException;
import com.bookstore.core.mapper.OrderMapper;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.order.OrderRepository;
import com.bookstore.core.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private Pageable pageable;
    @Mock
    private CartItemService cartItemService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("getOrders: Get orders by user email")
    public void getOrders_ValidEmailEmptyPagination_ReturnsOrders() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        Order order1 = getOrderTemplate(0);
        Order order2 = getOrderTemplate(1);
        OrderDto orderDto1 = getOrderDtoTemplate(0);
        OrderDto orderDto2 = getOrderDtoTemplate(1);
        List<OrderDto> expected = List.of(orderDto1, orderDto2);
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderRepository.findAllByUser(user, pageable))
                .thenReturn(List.of(order1, order2));
        when(orderMapper.toDto(order1)).thenReturn(orderDto1);
        when(orderMapper.toDto(order2)).thenReturn(orderDto2);

        // When
        List<OrderDto> actual = orderService.getOrders(email, pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrders: Get orders by non-existing user email")
    public void getOrders_NonExistingEmailEmptyPagination_ReturnsOrders() {
        // Given
        String email = "user@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrders(email, pageable));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addOrder: Add new order by valid email")
    public void addOrder_ValidEmail_ReturnsNewOrderDto() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        ShoppingCart shoppingCart = getShoppingCartTemplate(1);
        Order order = getOrderTemplate(2);
        OrderDto expected = getOrderDtoTemplate(2);
        when(userService.findByEmail(email)).thenReturn(user);
        when(shoppingCartService.getByUser(user)).thenReturn(shoppingCart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderDto actual = orderService.addOrder(email);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addOrder: Add new order by invalid email")
    public void addOrder_NonExistingEmail_ReturnsNewOrderDto() {
        // Given
        String email = "user@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.addOrder(email));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addOrder: Empty shopping cart")
    public void addOrder_EmptyShoppingCart_ReturnsNewOrderDto() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        ShoppingCart shoppingCart = getShoppingCartTemplate(1);
        ShoppingCart changedShoppingCart = new ShoppingCart()
                .setId(shoppingCart.getId())
                .setUser(shoppingCart.getUser())
                .setCartItems(new HashSet<>());
        when(userService.findByEmail(email)).thenReturn(user);
        when(shoppingCartService.getByUser(user)).thenReturn(changedShoppingCart);
        String expected = "Shopping cart is empty";

        // When
        Exception exception = assertThrows(OrderException.class,
                () -> orderService.addOrder(email));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderById: Get order by valid email and ID")
    public void getOrderById_ValidEmail_ReturnsOrderDto() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        Order order = getOrderTemplate(0);
        OrderDto expected = getOrderDtoTemplate(0);
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderDto actual = orderService.getOrderById(email, 1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderById: Get order by invalid email and valid ID")
    public void getOrderById_NonExistedEmail_Exception() {
        // Given
        String email = "user2@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userService.findByEmail(email)).thenThrow(
                new EntityNotFoundException(expected));

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderById(email, 1L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getOrderById: Get order by valid email and invalid ID")
    public void getOrderById_NonExistedId_Exception() {
        // Given
        Long id = 4L;
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        when(userService.findByEmail(email)).thenReturn(user);
        when(orderRepository.findByUserAndId(user, id)).thenReturn(Optional.empty());
        String expected = "Can't find order with ID: " + id;

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderById(email, id));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateOrderById: Update existing order by ID")
    public void updateOrderById_ValidId_ReturnsOrderDto() {
        // Given
        UpdateOrderDto requestDto = getUpdateOrderDtoTemplate(0);
        Order order = getOrderTemplate(0);
        OrderDto expected = getOrderDtoTemplate(0);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expected);

        // When
        OrderDto actual = orderService.updateOrderById(1L, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateOrderById: Update order by non-existing ID")
    public void updateOrderById_NonExistingId_Exception() {
        // Given
        Long id = 4L;
        UpdateOrderDto requestDto = getUpdateOrderDtoTemplate(0);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        String expected = "Can't find order with ID: " + id;

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.updateOrderById(id, requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndCheckByUser: Get order by valid user and ID")
    public void findByIdAndCheckByUser_ValidUserAndId_ReturnsOrderDto() {
        // Given
        Long id = 1L;
        User user = getUserTemplate(1);
        Order expected = getOrderTemplate(0);
        when(orderRepository.findByUserAndId(user, id)).thenReturn(Optional.of(expected));

        // When
        Order actual = orderService.findByIdAndCheckByUser(user, id);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndCheckByUser: Get order by non-existing ID or another user")
    public void findByIdAndCheckByUser_NonExistingId_Exception() {
        // Given
        Long id = 4L;
        User user = getUserTemplate(1);
        when(orderRepository.findByUserAndId(user, id)).thenReturn(Optional.empty());
        String expected = "Can't find order with ID: " + id;

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.findByIdAndCheckByUser(user, id));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }
}
