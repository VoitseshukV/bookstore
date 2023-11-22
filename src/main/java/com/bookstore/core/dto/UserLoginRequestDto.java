package com.bookstore.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 8, max = 32)
        String password
) {
}
