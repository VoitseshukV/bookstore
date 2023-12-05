package com.bookstore.core.controller;

import static com.bookstore.core.util.TestDataFactory.getBookDtoTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCategoryDtoTemplateById;
import static com.bookstore.core.util.TestDataFactory.getCreateCategoryRequestDtoTemplateById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.core.dto.BookDto;
import com.bookstore.core.dto.CategoryDto;
import com.bookstore.core.dto.CreateCategoryRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getAllCategories: Return list available categories")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = "classpath:database/category/fill-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCategories_CategoriesAvailable_ReturnsExpectedBooks() throws Exception {
        // Given
        CategoryDto categoryDto1 = getCategoryDtoTemplateById(0);
        CategoryDto categoryDto2 = getCategoryDtoTemplateById(1);
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<CategoryDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getCategoryById: Return category by valid ID")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = "classpath:database/category/fill-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidId_ReturnsFoundCategory() throws Exception {
        // Given
        CategoryDto expected = getCategoryDtoTemplateById(0);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories/1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getCategoryById: Get non-existing category")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getCategoryById_NonExistingCategory_Exception() throws Exception {
        // Given
        String expected = "Can't get category with id: 1";

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("getAllBooksByCategory: Return books by category")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = {
            "classpath:database/books/fill-books.sql",
            "classpath:database/category/fill-categories.sql",
            "classpath:database/books/fill-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/books/clear-book-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllBooksByCategory_CategoryAvailable_ReturnsExpectedBooks() throws Exception {
        // Given
        BookDto bookDto = getBookDtoTemplateById(2);
        List<BookDto> expected = List.of(bookDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories/2/books"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,
                "description,coverImage,categoryIds");
    }

    @Test
    @DisplayName("getAllBooksByCategory: Return books by non-existing category")
    @WithMockUser(username = "user@bookstore.ua")
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllBooksByCategory_NonExistingCategory_Exception() throws Exception {
        // Given
        String expected = "Can't get category with id: 1";

        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/categories/1/books"))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("createCategory: Add new valid category")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_ValidCategory_ReturnsCreatedCategoryDto() throws Exception {
        // Given
        CreateCategoryRequestDto categoryDto = getCreateCategoryRequestDtoTemplateById(0);
        CategoryDto expected = getCategoryDtoTemplateById(0);
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("createCategory: The category already exists")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/fill-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_CategoryAlreadyExists_Exception() throws Exception {
        // Given
        CreateCategoryRequestDto categoryDto = getCreateCategoryRequestDtoTemplateById(0);
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        // When
        Exception exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(
                                post("/api/categories")
                                        .content(jsonRequest)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isInternalServerError()));
        String actual = exception.getMessage();

        // Then
        assertTrue(actual.contains("Duplicate entry"));
    }

    @Test
    @DisplayName("updateCategoryById: Update existing category")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/fill-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategoryById_ValidCategory_ReturnsUpdatedCategoryDto() throws Exception {
        // Given
        CreateCategoryRequestDto categoryDto = getCreateCategoryRequestDtoTemplateById(0);
        CategoryDto expected = getCategoryDtoTemplateById(0);
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateCategoryById: Update non-existing category")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategoryById_NonExistingCategory_Exception() throws Exception {
        // Given
        CreateCategoryRequestDto categoryDto = getCreateCategoryRequestDtoTemplateById(0);
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);
        String expected = "Can't get category with id: 1";

        // When
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/categories/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("deleteCategoryById: Delete category by valid ID")
    @WithMockUser(username = "admin@bookstore.ua", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/fill-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategoryById_ValidId_DeleteCategory() throws Exception {
        // When and then
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}
