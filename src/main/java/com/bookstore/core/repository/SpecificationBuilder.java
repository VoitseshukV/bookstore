package com.bookstore.core.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T, K> {
    Specification<T> build(K searchParametersDto);
}
