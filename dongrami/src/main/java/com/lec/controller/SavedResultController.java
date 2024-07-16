package com.lec.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lec.dto.SavedResultDTO;
import com.lec.entity.Member;
import com.lec.entity.SavedResult;
import com.lec.service.SavedResultService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SavedResultController {
    
    @Autowired
    SavedResultService savedResultService;
    
    @PostMapping("/save")
    @ResponseBody
    public Map<String, Object> saveResult(@RequestParam("userId") String userId,
                                          @RequestParam("webReadingId") int webReadingId,
                                          @RequestParam("readingTitle") String readingTitle,
                                          @RequestParam("subId") int subId,
                                          @RequestParam("cardId") int cardId) throws ParseException {
        
        try {
            savedResultService.saveResult(userId, webReadingId, readingTitle, subId, cardId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "저장에 성공했습니다.");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "저장에 실패했습니다: " + e.getMessage());
            return response;
        }
    }
}


