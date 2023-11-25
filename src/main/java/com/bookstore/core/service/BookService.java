package com.bookstore.core.service;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id);
}
