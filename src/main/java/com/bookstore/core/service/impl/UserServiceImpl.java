package com.bookstore.core.service.impl;

import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.exception.EntityNotFoundException;
import com.bookstore.core.exception.RegistrationException;
import com.bookstore.core.mapper.UserMapper;
import com.bookstore.core.model.Role;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.role.RoleRepository;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.UserService;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user "
                    + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepository.getByName(Role.RoleName.USER)));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't get user with email: " + email));
    }
}
