package com.bookstore.core.service;

import com.bookstore.core.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
