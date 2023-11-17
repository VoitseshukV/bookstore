package com.bookstore.core.dto;

import com.bookstore.core.dto.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password",
        second = "repeatPassword",
        message = "The password fields must match")
public class UserRegistrationRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 8, max = 32)
    private String password;
    @NotBlank
    @Length(min = 8, max = 32)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
