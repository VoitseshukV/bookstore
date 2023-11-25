package com.bookstore.core.dto;

public record BookSearchParametersDto(
        String title,
        String author,
        String isbn,
        String description
) {
}
