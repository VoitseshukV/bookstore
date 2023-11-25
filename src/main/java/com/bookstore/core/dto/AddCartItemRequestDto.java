package com.bookstore.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequestDto(
        @NotNull
        Long bookId,
        @Min(1)
        Integer quantity
) {
}
