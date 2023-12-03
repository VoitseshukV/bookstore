package com.bookstore.core.dto;

import com.bookstore.core.dto.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "The password fields must match")
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8, max = 32)
    private String password;
    @NotBlank
    @Size(min = 8, max = 32)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
