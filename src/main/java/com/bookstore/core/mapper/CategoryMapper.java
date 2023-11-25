package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.bookstore.core.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
