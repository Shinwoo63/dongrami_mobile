package com.lec.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.lec.dto.WebReadingDTO;
import com.lec.entity.Member;
import com.lec.entity.WebReading;

@Repository
public interface ResultRepository extends JpaRepository<WebReading, Integer> {
	    
	    
    @Query(value = "SELECT w.web_reading_id, " +
            "w.reading1, " +
            "s.bubble_slack_name, " +
            "w.subcategory_id, " +
            "w.card_id, " +
            "c.image_url, " +
            "w.reading1_title " +
            "FROM web_reading w, cards c, subcategories s "+
            "WHERE w.subcategory_id = :subcategoryId " +
            "and w.card_id= c.card_id "+
            "and w.subcategory_id = s.subcategory_id " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
     List<Object[]> findOneCardReadings(@Param("subcategoryId") int subcategoryId);
}



