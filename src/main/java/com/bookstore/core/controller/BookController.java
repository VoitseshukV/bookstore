package com.bookstore.core.controller;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book catalog", description = "Endpoint for managing books list")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Get list of stored books")
    @GetMapping
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Add book", description = "Add new book to list")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Book details", description = "Get book data by ID")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Update book", description = "Update book information by ID")
    @PutMapping("/{id}")
    public BookDto updateBookById(@PathVariable Long id,
                                  @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.updateById(id, bookDto);
    }

    @Operation(summary = "Delete book", description = "Delete book entry by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Find books", description = "Find books by parameters")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
