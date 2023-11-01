package com.bookstore.core;

import com.bookstore.core.model.Book;
import com.bookstore.core.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Anansi Boys");
            book.setIsbn("9780060515195");
            book.setAuthor("Gaiman Neil");
            book.setPrice(BigDecimal.valueOf(500));
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
