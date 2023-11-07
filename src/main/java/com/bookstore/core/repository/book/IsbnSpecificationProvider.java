package com.bookstore.core.repository.book;

import com.bookstore.core.model.Book;
import com.bookstore.core.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    public static final String FIELD_NAME = "isbn";

    @Override
    public String getKey() {
        return FIELD_NAME;
    }

    public Specification<Book> getSpecification(String param) {
        return new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get(FIELD_NAME)),
                        param.toUpperCase());
            }
        };
    }
}
