package com.bookstore.core.security;

import com.bookstore.core.dto.UserLoginRequestDto;
import com.bookstore.core.dto.UserLoginResponseDto;
import com.bookstore.core.exception.LoginException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) throws LoginException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(),
                            requestDto.password()
                    )
            );
            return new UserLoginResponseDto(jwtUtil.generateToken(authentication.getName()));
        } catch (Exception e) {
            throw new LoginException("Can't login user " + requestDto.email(), e);
        }
    }
}
