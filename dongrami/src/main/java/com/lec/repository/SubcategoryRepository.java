package com.lec.repository;

import com.lec.entity.SavedResult;
import com.lec.entity.Subcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
	List<Subcategory> findTop5ByOrderByCountDesc();

    // 추가된 메서드
    Optional<Subcategory> findById(Integer subcategoryId);
}
