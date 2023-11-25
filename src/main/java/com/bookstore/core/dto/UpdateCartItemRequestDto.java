package com.bookstore.core.dto;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(
        @Min(1)
        Integer quantity
) {
}
