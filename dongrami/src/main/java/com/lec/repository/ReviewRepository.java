package com.lec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lec.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM review r JOIN FETCH r.member m JOIN FETCH r.subcategory s")
    List<Review> findAllWithDetails();

    @Query("SELECT r FROM review r JOIN FETCH r.member m JOIN FETCH r.subcategory s WHERE s.maincategory.maincategory_id = :mainCategoryId")
    List<Review> findByMainCategory(@Param("mainCategoryId") int mainCategoryId);

    void deleteByMemberUserId(String userId);
}
