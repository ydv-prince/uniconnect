package com.uniconnect.uniconnectbackend.config;

import com.uniconnect.uniconnectbackend.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }
}