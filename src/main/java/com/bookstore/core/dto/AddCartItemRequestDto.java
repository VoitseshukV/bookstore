package com.bookstore.core.dto;

import jakarta.validation.constraints.Min;

public record AddCartItemRequestDto(
        Long bookId,
        @Min(1)
        Integer quantity
) {
}
