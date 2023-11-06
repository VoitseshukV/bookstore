package com.bookstore.core.service.impl;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.BookMapper;
import com.bookstore.core.repository.BookRepository;
import com.bookstore.core.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(book)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.getBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book with id: " + id)));
    }
}
