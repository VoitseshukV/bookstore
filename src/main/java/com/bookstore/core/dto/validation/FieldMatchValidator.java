package com.bookstore.core.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private static final String DEFAULT_MESSAGE = "The fields must match";
    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
            valid = Objects.equals(firstObj, secondObj);
        } catch (ReflectiveOperationException e) {
            // ignore
        }
        if (!valid) {
            if (message.isBlank()) {
                context.buildConstraintViolationWithTemplate(DEFAULT_MESSAGE)
                        .addPropertyNode(firstFieldName)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            } else {
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
        }
        return valid;
    }
}
