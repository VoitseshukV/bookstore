package com.bookstore.core.service;

import com.bookstore.core.dto.OrderDto;
import com.bookstore.core.dto.UpdateOrderDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderDto> getOrders(String email, Pageable pageable);

    void addOrder(String email);

    OrderDto getOrderById(String email, Long orderId);

    OrderDto updateOrderById(Long orderId, UpdateOrderDto requestDto);
}
