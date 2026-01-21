package com.example.ecommerce.dto.reponse;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ApiResponseDTO<T> {
    private String message;
    private boolean success;
    private T data;
    private OffsetDateTime timestamp;
}
