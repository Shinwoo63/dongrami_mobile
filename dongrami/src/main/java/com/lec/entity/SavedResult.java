package com.lec.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "saved_result")
public class SavedResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private int resultId;

    @Column(name = "result_date", nullable = false)
    private Date resultDate;

    @Column(name = "position_1", length = 20, nullable = false)
    private String position1;

    @Column(name = "position_2", length = 20, nullable = true)
    private String position2;

    @Column(name = "position_3", length = 20, nullable = true)
    private String position3;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "card_id1", referencedColumnName = "card_id", nullable = false)
    private Card card1;

    @ManyToOne
    @JoinColumn(name = "card_id2", referencedColumnName = "card_id", nullable = true)
    private Card card2;

    @ManyToOne
    @JoinColumn(name = "card_id3", referencedColumnName = "card_id", nullable = true)
    private Card card3;

    @ManyToOne
    @JoinColumn(name = "web_reading_id", referencedColumnName = "web_reading_id", nullable = false)
    private WebReading webReading;

    @Builder
	public SavedResult(int resultId, Date resultDate, String position1, String position2, String position3,
			Member member, Card card1, Card card2, Card card3, WebReading webReading) {
		super();
		this.resultId = resultId;
		this.resultDate = resultDate;
		this.position1 = position1;
		this.position2 = position2;
		this.position3 = position3;
		this.member = member;
		this.card1 = card1;
		this.card2 = card2;
		this.card3 = card3;
		this.webReading = webReading;
	}
    
    
}
