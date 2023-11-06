package com.bookstore.core.repository;

import com.bookstore.core.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> getBookById(Long id);
}
