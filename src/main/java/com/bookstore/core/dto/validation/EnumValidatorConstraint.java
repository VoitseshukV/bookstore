package com.bookstore.core.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, String> {
    private String message;
    private Set<String> values;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        values = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
        message = "must be any of enum: " + Arrays.toString(
                Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                        .map(Enum::name)
                        .toArray(String[]::new));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = values.contains(value);
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
