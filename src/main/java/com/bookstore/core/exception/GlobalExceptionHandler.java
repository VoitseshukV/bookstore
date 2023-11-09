package com.bookstore.core.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> responseObject = new LinkedHashMap<>();
        responseObject.put("timestamp", LocalDateTime.now());
        responseObject.put("errors", ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList());
        return new ResponseEntity<>(responseObject, headers, status);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            return ((FieldError) objectError).getField() + " " + objectError.getDefaultMessage();
        }
        return objectError.getDefaultMessage();
    }
}
