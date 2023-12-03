package com.bookstore.core.service;

import static com.bookstore.core.util.TestDataFactory.getRoleTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserRegistrationRequestDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserResponseDtoTemplate;
import static com.bookstore.core.util.TestDataFactory.getUserTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.exception.RegistrationException;
import com.bookstore.core.mapper.UserMapper;
import com.bookstore.core.model.Role;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.role.RoleRepository;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("register: Register new user")
    public void register_ValidEmail_ReturnsUserResponseDto() throws RegistrationException {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDtoTemplate(1);
        UserResponseDto expected = getUserResponseDtoTemplate(1);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(user
                .setPassword(requestDto.getPassword()));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.getByName(Role.RoleName.USER)).thenReturn(getRoleTemplate(1));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);

        // When
        UserResponseDto actual = userService.register(requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("register: Register new user by existing email")
    public void register_ExistingEmail_Exception() {
        // Given
        String email = "user@bookstore.ua";
        User user = getUserTemplate(1);
        UserRegistrationRequestDto requestDto = getUserRegistrationRequestDtoTemplate(1);
        String expected = "Can't register user " + requestDto.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        Exception exception = assertThrows(RegistrationException.class,
                () -> userService.register(requestDto));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByEmail: Find existing user by email")
    public void findByEmail_ExistingEmail_ReturnsUser() {
        // Given
        String email = "user@bookstore.ua";
        User expected = getUserTemplate(1);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expected));

        // When
        User actual = userService.findByEmail(email);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByEmail: Find non-existing user by email")
    public void findByEmail_NonExistingEmail_Exception() {
        // Given
        String email = "user@bookstore.ua";
        String expected = "Can't get user with email: " + email;
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.findByEmail(email));
        String actual = exception.getMessage();

        // Then
        assertEquals(expected, actual);
    }
}
