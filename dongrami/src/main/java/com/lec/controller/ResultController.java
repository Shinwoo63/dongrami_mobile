package com.lec.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.Impl.ResultServiceImpl;
import com.lec.dto.WebReadingDTO;
import com.lec.entity.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class ResultController {

    @Autowired
    ResultServiceImpl resultService;

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    @PostMapping("/result")
    public String processResult(@RequestParam("subcategoryId") int subcategoryId, HttpSession session, Model model) {
    	
    	Member loggedInUser = (Member) session.getAttribute("loggedInUser");
	    if (loggedInUser != null) {
	        model.addAttribute("userId", loggedInUser.getUserId());
	    }
	    
	    
        try {
            List<WebReadingDTO> webReadings = resultService.getOneCardReadings(subcategoryId);
            if (!webReadings.isEmpty()) {
                WebReadingDTO webReading = webReadings.get(0);
                model.addAttribute("webReading", webReading);
            } else {
                model.addAttribute("error", "선택한 카테고리에 대한 타로 결과를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "타로 결과를 가져오는 중 오류가 발생했습니다.");
        }
        return "result";
    }
}
