package com.example.ecommerce.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private String id;
    private String reviewText;
    private double rating;
    private String reviewerName; // Name of the user who wrote the review
}
