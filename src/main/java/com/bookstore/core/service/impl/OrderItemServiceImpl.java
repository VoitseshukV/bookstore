package com.bookstore.core.service.impl;

import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.OrderItemMapper;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.order.OrderItemRepository;
import com.bookstore.core.repository.order.OrderRepository;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> getOrderItems(String email, Long orderId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        return orderRepository.findFirstByUserAndId(user, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId))
                .getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(String email, Long orderId, Long itemId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
        Order order = orderRepository.findFirstByUserAndId(user, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order with ID: " + orderId));
        return orderItemMapper.toDto(orderItemRepository.findFirstByOrderAndId(order, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item with ID: "
                        + itemId)));
    }
}
