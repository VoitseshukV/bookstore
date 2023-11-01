package com.bookstore.core.repository;

import com.bookstore.core.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
