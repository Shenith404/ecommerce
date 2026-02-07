package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.reponse.ReviewResponseDTO;
import com.example.ecommerce.model.Review;

public class ReviewMapper {
    public static ReviewResponseDTO toDto(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId().toString())
                .reviewText(review.getReviewText())
                .rating(review.getRating())
                .reviewerName(review.getUser().getFullName())
                .build();
    }
}
