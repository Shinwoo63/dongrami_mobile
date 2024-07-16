package com.lec.service;

import com.lec.dto.ReviewDTO;
import com.lec.entity.Review;

import java.util.List;

public interface ReviewService {
    void saveReview(Review review);
    void saveReview(ReviewDTO reviewDTO) throws Exception;
    void updateReview(int id, ReviewDTO reviewDTO) throws Exception;
    void deleteReview(int id);
    List<Review> getAllReviews();
    List<Review> getReviewsByMainCategory(int mainCategoryId);
    ReviewDTO getReviewById(int id);
}
