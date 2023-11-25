package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.CreateBookRequestDto;
import com.bookstore.core.model.Book;
import com.bookstore.core.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {

    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toUnmodifiableSet()));
    }

    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        if (requestDto.categoryIds() == null) {
            return;
        }
        book.setCategories(requestDto.categoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toUnmodifiableSet()));
    }
}
