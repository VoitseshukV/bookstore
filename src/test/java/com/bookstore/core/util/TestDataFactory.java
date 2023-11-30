package com.bookstore.core.util;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.bookstore.core.model.Book;
import com.bookstore.core.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestDataFactory {
    private static List<Category> categories;
    private static List<CategoryDto> categoryDtos;
    private static List<CreateCategoryRequestDto> categoryRequestDtos;
    private static List<Book> books;
    private static List<BookDto> bookDtos;
    private static List<CreateBookRequestDto> bookRequestDtos;
    private static List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIds;

    static {
        createCategories();
        createBooks();
    }

    public static Category getCategoryTemplateById(int id) {
        return categories.get(id);
    }

    public static CategoryDto getCategoryDtoTemplateById(int id) {
        return categoryDtos.get(id);
    }

    public static CreateCategoryRequestDto getCreateCategoryRequestDtoTemplateById(int id) {
        return categoryRequestDtos.get(id);
    }

    public static Book getBookTemplateById(int id) {
        return books.get(id);
    }

    public static BookDto getBookDtoTemplateById(int id) {
        return bookDtos.get(id);
    }

    public static CreateBookRequestDto getCreateBookRequestDtoTemplateById(int id) {
        return bookRequestDtos.get(id);
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIdsTemplateById(int id) {
        return bookDtoWithoutCategoryIds.get(id);
    }

    private static void createCategories() {
        categories = new ArrayList<>();
        categories.add(new Category()
                .setId(1L)
                .setName("Sci-Fi")
                .setDescription(""));
        categories.add(new Category()
                .setId(2L)
                .setName("Fantasy")
                .setDescription(""));
        categoryDtos = new ArrayList<>();
        categories.forEach(category -> categoryDtos.add(
                new CategoryDto(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                )
        ));
        categoryRequestDtos = new ArrayList<>();
        categories.forEach(category -> categoryRequestDtos.add(
                new CreateCategoryRequestDto(
                        category.getName(),
                        category.getDescription()
                )
        ));
    }

    private static void createBooks() {
        books = new ArrayList<>();
        books.add(new Book()
                .setId(1L)
                .setTitle("Anansi Boys")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780060515195")
                .setPrice(BigDecimal.valueOf(500))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(1))));
        books.add(new Book()
                .setId(2L)
                .setTitle("American Gods")
                .setAuthor("Gaiman Neil")
                .setIsbn("9780062896261")
                .setPrice(BigDecimal.valueOf(450))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(1))));
        books.add(new Book()
                .setId(3L)
                .setTitle("Elantris")
                .setAuthor("Brandon Sanderson")
                .setIsbn("9780765350374")
                .setPrice(BigDecimal.valueOf(420))
                .setDescription("")
                .setCoverImage("")
                .setCategories(Set.of(categories.get(0), categories.get(1))));
        bookDtos = new ArrayList<>();
        books.forEach(book -> bookDtos.add(
                new BookDto().setId(book
                        .getId())
                        .setTitle(book.getTitle())
                        .setAuthor(book.getAuthor())
                        .setIsbn(book.getIsbn())
                        .setPrice(book.getPrice())
                        .setDescription(book.getDescription())
                        .setCoverImage(book.getCoverImage())
                        .setCategoryIds(book.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toUnmodifiableSet())
                )
        ));
        bookRequestDtos = new ArrayList<>();
        books.forEach(book -> bookRequestDtos.add(
                new CreateBookRequestDto(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPrice(),
                        book.getDescription(),
                        book.getCoverImage(),
                        book.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toUnmodifiableSet())
                )
        ));
        bookDtoWithoutCategoryIds = new ArrayList<>();
        books.forEach(book -> bookDtoWithoutCategoryIds.add(
                        new BookDtoWithoutCategoryIds(
                                book.getId(),
                                book.getTitle(),
                                book.getAuthor(),
                                book.getIsbn(),
                                book.getPrice(),
                                book.getDescription(),
                                book.getCoverImage()
                )
        ));
    }
}
