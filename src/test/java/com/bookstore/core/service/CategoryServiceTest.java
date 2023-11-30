package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getCategoryDtoTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCategoryTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCreateCategoryRequestDtoTemplateById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.bookstore.core.mapper.CategoryMapper;
import com.bookstore.core.model.Category;
import com.bookstore.core.repository.category.CategoryRepository;
import com.bookstore.core.service.impl.CategoryServiceImpl;
import jakarta.persistence.EntityExistsException;
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

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    Pageable pageable;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("save: Saving new category")
    public void save_ValidCreateCategoryRequestDto_ReturnValidCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDtoTemplateById(0);
        Category category = getCategoryTemplateById(0);
        CategoryDto expected = getCategoryDtoTemplateById(0);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.save(requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("save: Add category with same name")
    public void save_ExistingCategory_Exception() {
        // Given
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDtoTemplateById(0);
        Category category = getCategoryTemplateById(0);
        String expected = "Category with name \"" + category.getName() + "\" already exists";
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenThrow(new EntityExistsException(expected));

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.save(requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAll: Empty pagination")
    public void findAll_EmptyPagination_ReturnsCategories() {
        // Given
        Category category1 = getCategoryTemplateById(0);
        Category category2 = getCategoryTemplateById(1);
        CategoryDto categoryDto1 = getCategoryDtoTemplateById(0);
        CategoryDto categoryDto2 = getCategoryDtoTemplateById(1);
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);
        when(categoryRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(List.of(category1, category2))
        );
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        // When
        List<CategoryDto> actual = categoryService.findAll(pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleteById: Delete category by existing ID")
    public void deleteById_ExistingId_Success() {
        // When
        categoryService.deleteById(1L);

        // Then
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("updateById: Update existing category")
    public void updateById_ExistingCategory_ReturnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDtoTemplateById(0);
        Category category = getCategoryTemplateById(0);
        CategoryDto expected = getCategoryDtoTemplateById(0);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.updateById(1L, requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateById: Update non-existing category")
    public void updateById_NonExistingCategory_Exception() {
        // Given
        CreateCategoryRequestDto requestDto = getCreateCategoryRequestDtoTemplateById(0);
        String expected = "Can't get category with id: " + 3L;
        when(categoryRepository.existsById(3L)).thenReturn(false);

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.updateById(3L, requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getById: Get category by valid ID")
    public void getById_ValidId_ReturnsCategory() {
        // Given
        Category category = getCategoryTemplateById(0);
        CategoryDto expected = getCategoryDtoTemplateById(0);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.getById(1L);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getById: Get category by non-existing ID")
    public void getById_NonExistingCategory_Exception() {
        // Given
        String expected = "Can't get category with id: " + 3L;
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.getById(3L));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }
}
