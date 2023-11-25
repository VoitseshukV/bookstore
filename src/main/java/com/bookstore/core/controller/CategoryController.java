package com.bookstore.core.controller;

import com.bookstore.core.dto.BookDtoWithoutCategoryIds;
import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.bookstore.core.service.BookService;
import com.bookstore.core.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Categories endpoint", description = "Endpoint for managing categories list")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Get all categories", description = "Get list of book categories")
    @GetMapping
    public List<CategoryDto> getAllBooks(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Category details", description = "Get category data by ID")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Get all books by category",
            description = "Get full book list by category")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getAllBooksByCategory(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }

    @Operation(summary = "Add category", description = "Add new category to list")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createBook(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(summary = "Update category", description = "Update category information by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategoryById(@PathVariable Long id,
                                  @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.updateById(id, requestDto);
    }

    @Operation(summary = "Delete category", description = "Delete category entry by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
