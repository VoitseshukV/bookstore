package com.bookstore.core.controller;

import static com.bookstore.core.util.TestDataFactory.getUserLoginRequestDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserRegistrationRequestDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserResponseDtoTemplate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.core.dto.UserLoginRequestDto;
import com.bookstore.core.dto.UserLoginResponseDto;
import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("register: Register new user by valid email")
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void register_NonExistingEmail_RegisterNewUser() throws Exception {
        // Given
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDtoTemplate(1);
        UserResponseDto expected = getUserResponseDtoTemplate(1);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        UserResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserResponseDto.class
        );
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("register: Register new user by existing email")
    @Sql(scripts = "classpath:database/user/fill-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void register_ExistingEmail_Exception() throws Exception {
        // Given
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDtoTemplate(1);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Can't register user " + requestDto.getEmail();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }

    @Test
    @DisplayName("login: Login with valid user")
    @Sql(scripts = "classpath:database/user/fill-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_ValidUser_ReturnsToken() throws Exception {
        // Given
        UserLoginRequestDto requestDto = getUserLoginRequestDtoTemplate(1);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // Then
        UserLoginResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserLoginResponseDto.class
        );
        assertNotNull(actual);
        assertFalse(actual.token().isBlank());
    }

    @Test
    @DisplayName("login: Login with non-existing user")
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/clear-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_InvalidUser_Exception() throws Exception {
        // Given
        UserLoginRequestDto requestDto = getUserLoginRequestDtoTemplate(1);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Can't login user " + requestDto.email();

        // When
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        // Then
        assertTrue(actual.contains(expected));
    }
}
