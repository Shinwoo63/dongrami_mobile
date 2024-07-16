package com.lec.controller;

import com.lec.dto.MemberDTO;
import com.lec.service.EditProfileService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/editprofile")
public class EditProfileController {

    private final EditProfileService editProfileService;

    @Autowired
    public EditProfileController(EditProfileService editProfileService) {
        this.editProfileService = editProfileService;
    }

    @GetMapping
    public String editProfileForm(Model model, Principal principal, @RequestParam(value = "updated", required = false) String updated) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userId = principal.getName().substring(0,10); // 사용자 이름 가져오기
        MemberDTO memberDTO = editProfileService.getMemberDTO(userId);
        if (memberDTO != null) {
            model.addAttribute("memberDTO", memberDTO);
            if (updated != null && updated.equals("true")) {
                model.addAttribute("updated", true);
            }
            return "editprofile";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("memberDTO") @Valid MemberDTO memberDTO, BindingResult result,
                                Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "editprofile";
        }

        if (!memberDTO.getPassword().equals(memberDTO.getPassword2())) {
            model.addAttribute("passwordError", "Passwords do not match.");
            return "editprofile";
        }

        String userId = principal.getName();
        editProfileService.updateMemberDTO(memberDTO, userId);
        return "redirect:/editprofile?updated=true";
    }

    @PostMapping("/delete")
    public String deleteProfile(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userId = principal.getName();
        editProfileService.deleteMember(userId);
        return "redirect:/logout";
    }
}
