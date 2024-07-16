package com.lec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.entity.Member;
import com.lec.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
    LoginService loginService;

	@PostMapping("/custom-login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
		System.out.println("/custom-login경로로 들어옴");
		System.out.println("email : " + email + ", password :" + password);
		
        Member authenticatedMember = loginService.authenticateUser(email, password); 
        System.out.println("login_controller --> 멤버있나...? : " + authenticatedMember);
        if (authenticatedMember != null) {
        	System.out.println("이메일로 가입을 했네요..비밀번호도 일치합니다...로그인 합니다.");
            session.setAttribute("loggedInUser", authenticatedMember);
            return "redirect:/";
        } else {
        	 System.out.println("엥....?");
             redirectAttributes.addFlashAttribute("loginError", "입력하신 아이디나 비밀번호가 다릅니다.");
             return "redirect:/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

