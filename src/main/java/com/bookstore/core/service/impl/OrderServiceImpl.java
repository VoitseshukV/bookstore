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
import com.bookstore.core.repository.cart.CartItemRepository;
import com.bookstore.core.repository.cart.ShoppingCartRepository;
import com.bookstore.core.repository.order.OrderRepository;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.OrderService;
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
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<OrderDto> getOrders(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void addOrder(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        ShoppingCart shoppingCart = shoppingCartRepository.getByUser(user);
        if (shoppingCart == null || shoppingCart.getCartItems().isEmpty()) {
            throw new OrderException("Shopping cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(user.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.CREATED);
        order.setOrderItems(new HashSet<>());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getPrice().multiply(
                    BigDecimal.valueOf(orderItem.getQuantity())));
            cartItemRepository.delete(cartItem);
        }
        order.setTotal(total);
        orderRepository.save(order);
        shoppingCart.setCartItems(new HashSet<>());
    }

    @Override
    public OrderDto getOrderById(String email, Long orderId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        return orderMapper.toDto(orderRepository.findFirstByUserAndId(user, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId)));
    }

    @Override
    @Transactional
    public OrderDto updateOrderById(Long orderId, UpdateOrderDto requestDto) {
        Order order = orderRepository.findFirstById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId));
        order.setStatus(Order.OrderStatus.valueOf(requestDto.status()));
        orderRepository.save(order);
        return null;
    }
}
