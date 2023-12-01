package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getBookDtoTemplateById;
import static com.bookstore.core.util.TestDataFactory.getBookDtoWithoutCategoryIdsTemplateById;
import static com.bookstore.core.util.TestDataFactory.getBookTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCreateBookRequestDtoTemplateById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.mapper.impl.BookMapperImpl;
import com.bookstore.core.model.Book;
import com.bookstore.core.repository.book.BookRepository;
import com.bookstore.core.repository.book.BookSpecificationBuilder;
import com.bookstore.core.repository.category.CategoryRepository;
import com.bookstore.core.service.impl.BookServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapperImpl bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    Pageable pageable;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("save: Saving new book")
    public void save_ValidCreateBookRequestDto_ReturnValidBookDto() {
        // Given
        CreateBookRequestDto requestDto = getCreateBookRequestDtoTemplateById(0);
        Book book = getBookTemplateById(0);
        BookDto expected = getBookDtoTemplateById(0);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.save(requestDto);

        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "categoryIds");
    }

    @Test
    @DisplayName("save: Add book with same ISBN")
    public void save_ExistingBook_Exception() {
        // Given
        CreateBookRequestDto requestDto = getCreateBookRequestDtoTemplateById(0);
        Book book = getBookTemplateById(0);
        String expected = "Book with ISBN \"" + book.getIsbn() + "\" already exists";
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenThrow(new EntityExistsException(expected));

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.save(requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAllByCategoryId: Find one book by category Sci-Fi")
    public void findAllByCategoryId_CategorySciFi_ReturnsOneBook() {
        // Given
        BookDtoWithoutCategoryIds bookDto = getBookDtoWithoutCategoryIdsTemplateById(2);
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);
        Book book = getBookTemplateById(2);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        // When
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(1L);

        // Then
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAllByCategoryId: Find three books by category Fantasy")
    public void findAllByCategoryId_CategoryFantasy_ReturnsThreeBooks() {
        // Given
        BookDtoWithoutCategoryIds bookDto1 = getBookDtoWithoutCategoryIdsTemplateById(0);
        BookDtoWithoutCategoryIds bookDto2 = getBookDtoWithoutCategoryIdsTemplateById(1);
        BookDtoWithoutCategoryIds bookDto3 = getBookDtoWithoutCategoryIdsTemplateById(2);
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto1, bookDto2, bookDto3);
        Book book1 = getBookTemplateById(0);
        Book book2 = getBookTemplateById(1);
        Book book3 = getBookTemplateById(2);
        when(categoryRepository.existsById(2L)).thenReturn(true);
        when(bookRepository.findAllByCategoryId(2L)).thenReturn(List.of(book1, book2, book3));
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDto2);
        when(bookMapper.toDtoWithoutCategories(book3)).thenReturn(bookDto3);

        // When
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(2L);

        // Then
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAllByCategoryId: Find books by a non-existent category")
    public void findAllByCategoryId_ExpectedCategory_Exception() {
        // Given
        String expected = "Can't get category with id: " + 3L;
        when(categoryRepository.existsById(3L)).thenReturn(false);

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.findAllByCategoryId(3L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAllByCategoryId: Find books by null category ID")
    public void findAllByCategoryId_NullCategoryId_Exception() {
        // Given
        String expected = "Can't get category with id: " + null;
        when(categoryRepository.existsById(null)).thenReturn(false);

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.findAllByCategoryId(null));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateById: Update existing book")
    public void updateById_ExistingBook_ReturnsBookDto() {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(2);
        Book book = getBookTemplateById(2);
        BookDto expected = getBookDtoTemplateById(2);
        when(bookRepository.existsById(3L)).thenReturn(true);
        when(bookMapper.toModel(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.updateById(3L, bookDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateById: Update non-existing book")
    public void updateById_NonExistingBook_Exception() {
        // Given
        CreateBookRequestDto bookDto = getCreateBookRequestDtoTemplateById(2);
        String expected = "Can't get book with id: " + 4L;
        when(bookRepository.existsById(4L)).thenReturn(false);

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.updateById(4L, bookDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getById: Get book by valid ID")
    public void getById_ValidId_ReturnsBook() {
        // Given
        Book expected = getBookTemplateById(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expected));

        // When
        Book actual = bookService.getById(1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getById: Get book by non-existing ID")
    public void getById_NonExistingBook_Exception() {
        // Given
        String expected = "Can't get book with id: " + 4L;
        when(bookRepository.findById(4L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getById(4L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getDtoById: Get book by valid ID")
    public void getDtoById_ValidId_ReturnsBook() {
        // Given
        Book book = getBookTemplateById(0);
        BookDto expected = getBookDtoTemplateById(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.getDtoById(1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getDtoById: Get book by non-existing ID")
    public void getDtoById_NonExistingBook_Exception() {
        // Given
        String expected = "Can't get book with id: " + 4L;
        when(bookRepository.findById(4L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getDtoById(4L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search: Book found")
    public void search_BookFound_ReturnsOneBook() {
        // Given
        BookSearchParametersDto parameters = new BookSearchParametersDto(
                "Anansi", "", "", ""
        );

        Book book = getBookTemplateById(0);
        BookDto bookDto = getBookDtoTemplateById(0);
        List<BookDto> expected = List.of(bookDto);
        when(bookSpecificationBuilder.build(parameters))
                .thenReturn(new Specification<Book>() {
            @Override
            public Predicate toPredicate(
                    Root<Book> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder
            ) {
                return null;
            }
        });
        when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        List<BookDto> actual = bookService.search(parameters);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search: Book not found")
    public void search_BookNotFound_ReturnsEmptyList() {
        // Given
        BookSearchParametersDto parameters = new BookSearchParametersDto(
                "Fahrenheit", "", "", ""
        );
        when(bookSpecificationBuilder.build(parameters))
                .thenReturn(new Specification<Book>() {
            @Override
            public Predicate toPredicate(
                    Root<Book> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder
            ) {
                return null;
            }
        });
        when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // When
        List<BookDto> actual = bookService.search(parameters);

        // Then
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("findAll: Empty pagination")
    public void findAll_EmptyPagination_ReturnsBooks() {
        // Given
        Book book1 = getBookTemplateById(0);
        Book book2 = getBookTemplateById(1);
        Book book3 = getBookTemplateById(2);
        BookDto bookDto1 = getBookDtoTemplateById(0);
        BookDto bookDto2 = getBookDtoTemplateById(1);
        BookDto bookDto3 = getBookDtoTemplateById(2);
        List<BookDto> expected = List.of(bookDto1, bookDto2, bookDto3);
        when(bookRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(List.of(book1, book2, book3))
        );
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);
        when(bookMapper.toDto(book3)).thenReturn(bookDto3);

        // When
        List<BookDto> actual = bookService.findAll(pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleteById: Delete by existing ID")
    public void deleteById_ExistingId_Success() {
        // When
        bookService.deleteById(1L);

        // Then
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
