package com.example.ecommerce.exception;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import jakarta.xml.bind.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice()
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add( error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(ApiResponseDTO.builder()
                .message(errors.stream().findFirst().orElse("Validation Error"))
                .success(false)
                .data(errors)
                .timestamp(OffsetDateTime.now())
                .build());

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("ResourceNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ApiResponseDTO.builder()
                .message(ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        log.warn("ValidationException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponseDTO.builder()
                .message(ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.warn("Username not found : {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponseDTO.builder()
                .message(ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponseDTO.builder()
                .message(ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> handleAccessException(IllegalAccessException ex) {
        log.error("IllegalAccessException: {}", ex.getMessage());
        return ResponseEntity.status(403).body(ApiResponseDTO.builder()
                .message("Access operation failed: " + ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        log.error("IOException: {}", ex.getMessage());
        return ResponseEntity.status(400).body(ApiResponseDTO.builder()
                .message("IO operation failed: " + ex.getMessage())
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access Denied: {}", ex.getMessage());
        return ResponseEntity.status(403).body(ApiResponseDTO.builder()
                .message("Access Denied: You don't have permission to access this resource")
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        return ResponseEntity.status(500).body(ApiResponseDTO.builder()
                .message("Internal Server Error")
                .success(false)
                .data(null)
                .timestamp(OffsetDateTime.now())
                .build());
    }


}
