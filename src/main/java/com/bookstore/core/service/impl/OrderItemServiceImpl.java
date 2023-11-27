package com.bookstore.core.service.impl;

import com.bookstore.core.dto.OrderItemDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.OrderItemMapper;
import com.bookstore.core.model.Order;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.order.OrderItemRepository;
import com.bookstore.core.service.OrderItemService;
import com.bookstore.core.service.OrderService;
import com.bookstore.core.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> getOrderItems(String email, Long orderId) {
        User user = userService.findByEmail(email);
        return orderService.findFirstByUserAndId(user, orderId).getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(String email, Long orderId, Long itemId) {
        User user = userService.findByEmail(email);
        Order order = orderService.findFirstByUserAndId(user, orderId);
        return orderItemMapper.toDto(orderItemRepository.findFirstByOrderAndId(order, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item with ID: "
                        + itemId)));
    }
}
