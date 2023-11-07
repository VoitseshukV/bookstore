package com.bookstore.core.service.impl;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.mapper.BookMapper;
import com.bookstore.core.model.Book;
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
    public BookDto save(CreateBookRequestDto bookDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(bookDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book with id: " + id)));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookDto) {
        Book existing = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book with id: " + id));
        existing.setTitle(bookDto.getTitle());
        existing.setAuthor(bookDto.getAuthor());
        existing.setIsbn(bookDto.getIsbn());
        existing.setPrice(bookDto.getPrice());
        existing.setDescription(bookDto.getDescription());
        existing.setCoverImage(bookDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(existing));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
