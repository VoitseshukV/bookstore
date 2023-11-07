package com.bookstore.core.repository.book;

import com.bookstore.core.dto.BookSearchParametersDto;
import com.bookstore.core.model.Book;
import com.bookstore.core.repository.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book,
        BookSearchParametersDto> {
    private final BookSpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParametersDto.title() != null
                && !bookSearchParametersDto.title().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.title()));
        }
        if (bookSearchParametersDto.author() != null
                && !bookSearchParametersDto.author().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.author()));
        }
        if (bookSearchParametersDto.isbn() != null
                && !bookSearchParametersDto.isbn().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParametersDto.isbn()));
        }
        if (bookSearchParametersDto.description() != null
                && !bookSearchParametersDto.description().isBlank()) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider("description")
                    .getSpecification(bookSearchParametersDto.description()));
        }
        return specification;
    }
}
