package com.lec.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.entity.Card;
import com.lec.entity.Member;
import com.lec.entity.SavedResult;
import com.lec.entity.WebReading;
import com.lec.repository.CardRepository;
import com.lec.repository.MemberRepository;
import com.lec.repository.SavedResultRepository;
import com.lec.repository.SubcategoryRepository;
import com.lec.repository.WebReadingRepository;
import com.lec.service.SavedResultService;

import jakarta.transaction.Transactional;

@Service
public class SavedResultImpl implements SavedResultService{
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	SubcategoryRepository subcategoryRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	WebReadingRepository webReadingRepository;
	
	@Autowired
	SavedResultRepository savedResultRepository;
	
	@Override
	@Transactional
	public void saveResult(String userId, int webReadingId, String readingTitle, int subId, int cardId) throws ParseException {
		
		Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WebReading webReading = webReadingRepository.findById(webReadingId)
                .orElseThrow(() -> new RuntimeException("WebReading not found"));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        SavedResult savedResult = SavedResult.builder()
                .resultDate(getCurrentDate())
                .position1(readingTitle)
                .position2(null)  // 필요에 따라 설정
                .position3(null)  // 필요에 따라 설정
                .member(member)
                .card1(card)
                .card2(null)  // 필요에 따라 설정
                .card3(null)  // 필요에 따라 설정
                .webReading(webReading)
                .build();

        savedResultRepository.save(savedResult);
	}
	
	// 생성일자 구하는 로직
		public Date getCurrentDate() throws ParseException {
		    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    	String dateString = dateFormat.format(new Date());
		    	return dateFormat.parse(dateString);
		    }

}
