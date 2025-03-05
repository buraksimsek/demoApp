package com.example.demo.exception;

import java.util.stream.Collectors;

import com.example.demo.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
        MethodArgumentNotValidException ex) {

        var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> String.format(
                "%s: %s",
                fieldError.getField(),
                fieldError.getDefaultMessage()
            ))
            .collect(Collectors.toList());

        return ResponseEntity
            .badRequest()
            .body(new ApiResponse<>(errors));
    }

}



