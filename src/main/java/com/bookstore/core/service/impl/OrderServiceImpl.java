package com.bookstore.core.service.impl;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.UpdateOrderDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.exception.OrderException;
import com.bookstore.core.mapper.OrderMapper;
import com.bookstore.core.model.CartItem;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.OrderItem;
import com.bookstore.core.model.ShoppingCart;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.order.OrderRepository;
import com.bookstore.core.service.CartItemService;
import com.bookstore.core.service.OrderService;
import com.bookstore.core.service.ShoppingCartService;
import com.bookstore.core.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public List<OrderDto> getOrders(String email, Pageable pageable) {
        User user = userService.findByEmail(email);
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto addOrder(String email) {
        User user = userService.findByEmail(email);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(user);
        if (shoppingCart == null || shoppingCart.getCartItems().isEmpty()) {
            throw new OrderException("Shopping cart is empty");
        }
        Order order = fillOrderByShoppingCart(shoppingCart);
        orderRepository.save(order);
        shoppingCart.setCartItems(new HashSet<>());
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getOrderById(String email, Long orderId) {
        User user = userService.findByEmail(email);
        return orderMapper.toDto(orderRepository.findByUserAndId(user, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId)));
    }

    @Override
    @Transactional
    public OrderDto updateOrderById(Long orderId, UpdateOrderDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId));
        order.setStatus(Order.OrderStatus.valueOf(requestDto.status()));
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Order findFirstByUserAndId(User user, Long orderId) {
        return orderRepository.findByUserAndId(user, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId));
    }

    private Order fillOrderByShoppingCart(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setShippingAddress(shoppingCart.getUser().getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.CREATED);
        order.setOrderItems(new HashSet<>());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = addByCartItem(cartItem, order);
            total = total.add(orderItem.getPrice().multiply(
                    BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotal(total);
        return order;
    }

    @Override
    public OrderItem addByCartItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        order.getOrderItems().add(orderItem);
        cartItemService.delete(cartItem);
        return orderItem;
    }
}
