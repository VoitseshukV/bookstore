package com.bookstore.core.service;

import com.bookstore.core.dto.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getOrderItems(String email, Long orderId);

    OrderItemDto getOrderItemById(String email, Long orderId, Long itemId);
}
