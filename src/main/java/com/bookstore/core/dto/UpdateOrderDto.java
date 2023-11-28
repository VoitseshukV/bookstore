package com.bookstore.core.dto;

import com.bookstore.core.dto.validation.EnumValidator;
import com.bookstore.core.model.Order;

public record UpdateOrderDto(
        @EnumValidator(enumClass = Order.OrderStatus.class)
        String status
) {
}
