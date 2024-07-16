package com.lec.service;

import java.text.ParseException;

import com.lec.entity.SavedResult;

public interface SavedResultService {
	
	public void saveResult(String userId, int webReadingId, String readingTitle, int subId, int cardId) throws ParseException;

}
