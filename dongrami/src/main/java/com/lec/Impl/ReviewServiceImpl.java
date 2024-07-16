package com.lec.Impl;

import com.lec.dto.ReviewDTO;
import com.lec.entity.Member;
import com.lec.entity.Review;
import com.lec.entity.Subcategory;
import com.lec.repository.MemberRepository;
import com.lec.repository.ReviewRepository;
import com.lec.repository.SubcategoryRepository;
import com.lec.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public void saveReview(ReviewDTO reviewDTO) throws Exception {
        Optional<Member> memberOptional = memberRepository.findById(reviewDTO.getUserId());
        if (!memberOptional.isPresent()) {
            throw new Exception("User not found");
        }
        Member member = memberOptional.get();

        Optional<Subcategory> subcategoryOptional = subcategoryRepository.findById(reviewDTO.getSubcategoryId());
        if (!subcategoryOptional.isPresent()) {
            throw new Exception("Subcategory not found");
        }
        Subcategory subcategory = subcategoryOptional.get();

        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .reviewText(reviewDTO.getReviewText())
                .reviewCreate(new java.util.Date())
                .member(member)
                .subcategory(subcategory)
                .build();
        reviewRepository.save(review);
    }

    @Override
    public void updateReview(int id, ReviewDTO reviewDTO) throws Exception {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setRating(reviewDTO.getRating());
            review.setReviewText(reviewDTO.getReviewText());
            review.setReviewModify(new java.util.Date());
            reviewRepository.save(review);
        } else {
            throw new Exception("Review not found");
        }
    }

    @Override
    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAllWithDetails();
    }

    @Override
    public List<Review> getReviewsByMainCategory(int mainCategoryId) {
        return reviewRepository.findByMainCategory(mainCategoryId);
    }

    @Override
    public ReviewDTO getReviewById(int id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            return new ReviewDTO(review);
        } else {
            return null;
        }
    }
}
