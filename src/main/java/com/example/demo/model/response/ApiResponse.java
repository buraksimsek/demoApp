package com.example.demo.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private T data;

    private String error;

    private List<String> validationErrors;

    public ApiResponse(T data) {
        this.data = data;
    }

    /**
     * Error response constructor.
     */
    public ApiResponse(String error) {
        this.error = error;
    }

    /**
     * Validation error response constructor.
     */
    public ApiResponse(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

}
