package com.lec.service;

import com.lec.dto.MemberDTO;
import com.lec.entity.Member;
import com.lec.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
public class EditProfileService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReplyRepository replyRepository;
    private final VoteResultRepository voteResultRepository;
    private final SavedResultRepository savedResultRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public EditProfileService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                              ReplyRepository replyRepository, VoteResultRepository voteResultRepository,
                              SavedResultRepository savedResultRepository, ReviewRepository reviewRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.replyRepository = replyRepository;
        this.voteResultRepository = voteResultRepository;
        this.savedResultRepository = savedResultRepository;
        this.reviewRepository = reviewRepository;
    }

    public MemberDTO getMemberDTO(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setEmail(member.getEmail());
            memberDTO.setNickname(member.getNickname());
            memberDTO.setPhoneNumber(member.getPhoneNumber());
            if (member.getBirthDate() != null) {
                // Extract year, month, day from birthDate
                memberDTO.setBirthYear(String.valueOf(member.getBirthDate().getYear() + 1900));
                memberDTO.setBirthMonth(String.format("%02d", member.getBirthDate().getMonth() + 1));
                memberDTO.setBirthDay(String.format("%02d", member.getBirthDate().getDate()));
            }
            memberDTO.setGender(member.getGender());
            return memberDTO;
        }
        return null;
    }

    @Transactional
    public void updateMemberDTO(MemberDTO memberDTO, String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
                member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            }
            member.setNickname(memberDTO.getNickname());
            member.setPhoneNumber(memberDTO.getPhoneNumber());
            
            // Update birthDate from birthYear, birthMonth, birthDay
            if (memberDTO.getBirthYear() != null && memberDTO.getBirthMonth() != null && memberDTO.getBirthDay() != null) {
                int year = Integer.parseInt(memberDTO.getBirthYear());
                int month = Integer.parseInt(memberDTO.getBirthMonth());
                int day = Integer.parseInt(memberDTO.getBirthDay());
                Date birthDate = new Date(year - 1900, month - 1, day);
                member.setBirthDate(birthDate);
            }
            
            memberRepository.save(member);
        }
    }

    @Transactional
    public void deleteMember(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // Delete associated data in order: review -> saved_result -> reply -> vote_result
            deleteReviewsByUserId(userId);
            deleteSavedResultsByUserId(userId);
            deleteRepliesByUserId(userId);
            deleteVoteResultsByUserId(userId);
            // Finally delete the member itself
            memberRepository.delete(member);
        }
    }

    @Transactional
    public void deleteReviewsByUserId(String userId) {
        reviewRepository.deleteByMemberUserId(userId);
    }

    @Transactional
    public void deleteRepliesByUserId(String userId) {
        replyRepository.deleteByMemberUserId(userId);
    }

    @Transactional
    public void deleteVoteResultsByUserId(String userId) {
        voteResultRepository.deleteByMemberUserId(userId);
    }

    @Transactional
    public void deleteSavedResultsByUserId(String userId) {
        savedResultRepository.deleteByMemberUserId(userId);
    }
}
