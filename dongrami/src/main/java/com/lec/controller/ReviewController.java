package com.lec.controller;

import com.lec.dto.ReviewDTO;
import com.lec.entity.Review;
import com.lec.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review")
    public String showReviewPage(HttpSession session, Model model) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("currentUserId", loggedInUser.toString());
        } else {
            model.addAttribute("currentUserId", "unknown");
        }
        return "review";
    }

    @PostMapping("/review/api/reviews")
    @ResponseBody
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            reviewService.saveReview(reviewDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰가 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰 저장에 실패했습니다. 에러: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/review/api/reviews/{id}")
    @ResponseBody
    public ResponseEntity<?> updateReview(@PathVariable("id") int id, @RequestBody ReviewDTO reviewDTO) {
        try {
            reviewService.updateReview(id, reviewDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰가 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰 수정에 실패했습니다. 에러: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/review/api/reviews/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteReview(@PathVariable("id") int id) {
        try {
            reviewService.deleteReview(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰가 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰 삭제에 실패했습니다. 에러: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/review/api/reviews")
    @ResponseBody
    public ResponseEntity<List<Review>> getAllReviews() {
        try {
            List<Review> reviews = reviewService.getAllReviews();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/review/api/reviews/{mainCategoryId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getReviewsByMainCategory(@PathVariable("mainCategoryId") int mainCategoryId) {
        try {
            List<Review> reviews = reviewService.getReviewsByMainCategory(mainCategoryId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/review/api/review/{id}")
    @ResponseBody
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable("id") int id) {
        try {
            ReviewDTO reviewDTO = reviewService.getReviewById(id);
            return ResponseEntity.ok(reviewDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
