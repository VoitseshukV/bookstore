package com.bookstore.core.service;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto getBookById(Long id);
}
