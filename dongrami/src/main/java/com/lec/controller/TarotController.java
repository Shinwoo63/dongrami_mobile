package com.lec.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.entity.Subcategory;
import com.lec.service.SubcategoryService;

@Controller
class TarotController {
	
	@Autowired
	SubcategoryService subcategoryService;
	
	@GetMapping("/tarot")
	public String tarot() {
		return "tarot";
	}
	
	@PostMapping("/tarot")
	public String processTarot(@RequestParam("categoryId") int categoryId, Model model) {
	    Optional<Subcategory> subcategoryOpt = subcategoryService.findById(categoryId);
	    
	    if (subcategoryOpt.isPresent()) {
	        Subcategory subcategory = subcategoryOpt.get();
	        model.addAttribute("subcategory", subcategory);
	        model.addAttribute("sub_id", subcategory.getSubcategory_id());
	        model.addAttribute("subcategoryBubble", subcategory.getBubble_slack_name());
	        // 필요한 경우 추가 속성들을 모델에 담습니다.
	        
	        return "tarot";
	    } else {
	        // 서브카테고리를 찾지 못한 경우의 처리
	        return "error";  // 또는 다른 적절한 처리
	    }
	}
	
}
