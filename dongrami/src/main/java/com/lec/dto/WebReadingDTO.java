package com.lec.dto;

import com.lec.entity.WebReading;

import com.lec.entity.Subcategory;
import com.lec.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebReadingDTO {
    private int webReadingId;
    private String reading1;
    private String reading2;
    private String reading3;
    private String reading1Title;
    private String reading2Title;
    private String reading3Title;
    private Subcategory subcategory;  // int subcategoryId 대신
    private Card card;  // int cardId 대신
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String bubbleSlackName;  // 추가된 필드

    // 생성자 -- 해석이 하나인 경우
    public WebReadingDTO(int webReadingId, String reading1, String reading1Title, Subcategory subcategory, Card card,
                         String imageUrl1, String bubbleSlackName) {
        this.webReadingId = webReadingId;
        this.reading1 = reading1;
        this.reading1Title = reading1Title;
        this.subcategory = subcategory;
        this.card = card;
        this.imageUrl1 = imageUrl1;
        this.bubbleSlackName = bubbleSlackName;
    }
    
    public WebReading toEntity() {
        return WebReading.builder()
                .webReadingId(webReadingId)
                .reading1(reading1)
                .reading2(reading2)
                .reading3(reading3)
                .reading1Title(reading1Title)
                .reading2Title(reading2Title)
                .reading3Title(reading3Title)
                .subcategory(subcategory)
                .card(card)
                .build();
    }
}

