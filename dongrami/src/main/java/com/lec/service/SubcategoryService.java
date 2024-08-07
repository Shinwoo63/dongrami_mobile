package com.lec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.entity.Subcategory;
import com.lec.repository.SubcategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class SubcategoryService {
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    @Transactional
    public void updateClickCount(int subcategoryId, int count) {
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(subcategoryId);
        optionalSubcategory.ifPresent(subcategory -> {
            subcategory.setCount(count);
            subcategoryRepository.save(subcategory); // Update count in DB
        });
    }
    
    public List<Subcategory> getTop5SubcategoriesByCount() {
        return subcategoryRepository.findTop5ByOrderByCountDesc();
    }

    public Optional<Subcategory> findById(int id) {
        return subcategoryRepository.findById(id);
    }
}