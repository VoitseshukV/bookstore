package com.bookstore.core.service.impl;

import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.exception.RegistrationException;
import com.bookstore.core.mapper.UserMapper;
import com.bookstore.core.model.User;
import com.bookstore.core.repository.user.UserRepository;
import com.bookstore.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user "
                    + requestDto.getEmail());
        }
        User savedUser = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.toDto(savedUser);
    }
}
