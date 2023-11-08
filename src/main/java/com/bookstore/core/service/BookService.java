package com.bookstore.core.service;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
