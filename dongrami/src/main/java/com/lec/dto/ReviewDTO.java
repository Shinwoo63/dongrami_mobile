package com.lec.dto;

import java.util.Date;

import com.lec.entity.Review;

public class ReviewDTO {

    private int reviewId;
    private int rating;
    private String reviewText;
    private Date reviewCreate;
    private Date reviewModify;
    private String userId;
    private int subcategoryId;
    private int resultId;

    // 기본 생성자, 게터, 세터 추가
    public ReviewDTO() {}

    public ReviewDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.reviewCreate = review.getReviewCreate();
        this.reviewModify = review.getReviewModify();
        this.userId = review.getMember().getUserId();
        this.subcategoryId = review.getSubcategory().getSubcategory_id();
        this.resultId = review.getSavedResult() != null ? review.getSavedResult().getResultId() : 0;
    }

    // 게터와 세터
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public Date getReviewCreate() { return reviewCreate; }
    public void setReviewCreate(Date reviewCreate) { this.reviewCreate = reviewCreate; }
    public Date getReviewModify() { return reviewModify; }
    public void setReviewModify(Date reviewModify) { this.reviewModify = reviewModify; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(int subcategoryId) { this.subcategoryId = subcategoryId; }
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }
}
