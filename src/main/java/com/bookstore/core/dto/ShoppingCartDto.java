package com.bookstore.core.dto;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private String email;
    private Set<CartItemDto> cartItems;
}
