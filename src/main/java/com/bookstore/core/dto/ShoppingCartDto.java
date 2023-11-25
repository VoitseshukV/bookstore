package com.bookstore.core.dto;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private String email;
    private Set<CartItemDto> cartItems;
}
