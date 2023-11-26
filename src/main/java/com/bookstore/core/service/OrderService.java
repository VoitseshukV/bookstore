package com.bookstore.core.service;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.UpdateOrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrders(String email);

    void addOrder(String email);

    OrderDto getOrderById(String email, Long orderId);

    OrderDto updateOrderById(Long orderId, UpdateOrderDto requestDto);
}
