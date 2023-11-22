package com.bookstore.core.controller;

import com.bookstore.core.dto.UserLoginRequestDto;
import com.bookstore.core.dto.UserLoginResponseDto;
import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.exception.LoginException;
import com.bookstore.core.exception.RegistrationException;
import com.bookstore.core.security.AuthenticationService;
import com.bookstore.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication endpoint",
        description = "Endpoint for register and login")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user", description = "New user registration")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "Login user", description = "Login existing user")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto)
            throws LoginException {
        return authenticationService.authenticate(requestDto);
    }
}
