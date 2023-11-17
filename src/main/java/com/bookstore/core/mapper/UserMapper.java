package com.bookstore.core.mapper;

import com.bookstore.core.config.MapperConfig;
import com.bookstore.core.dto.UserRegistrationRequestDto;
import com.bookstore.core.dto.UserResponseDto;
import com.bookstore.core.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
