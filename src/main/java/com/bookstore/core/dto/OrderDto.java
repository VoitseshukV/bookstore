package com.bookstore.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderDto {
    private Long id;
    private String email;
    private String status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}
