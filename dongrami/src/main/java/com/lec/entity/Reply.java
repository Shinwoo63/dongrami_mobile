package com.lec.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@Getter
@Entity
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private int replyId;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "reply_create", nullable = false)
    private  LocalDateTime replyCreate;

    @Column(name = "reply_modify")
    private  LocalDateTime replyModify;

    @Column(name = "parent_re_id", nullable = false)
    private int parentReId;

    @ManyToOne
    @JoinColumn(name = "vote_id", referencedColumnName = "vote_id", nullable = false)  // 이 부분 확인
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
    
   
    
    
    
    @Builder
	public Reply(int replyId, String content, int level,  LocalDateTime replyCreate,  LocalDateTime replyModify, int parentReId, Vote vote,
			Member member) {
		super();
		this.replyId = replyId;
		this.content = content;
		this.level = level;
		this.replyCreate = replyCreate;
		this.replyModify = replyModify;
		this.parentReId = parentReId;
		this.vote = vote;
		this.member = member;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setReplyCreate(LocalDateTime replyCreate) {
		this.replyCreate = replyCreate;
	}
	public void setReplyModify(LocalDateTime replyModify) {
		this.replyModify = replyModify;
	}
	public void setParentReId(int parentReId) {
		this.parentReId = parentReId;
	}
	public void setVote(Vote vote) {
		this.vote = vote;
	}
	public void setMember(Member member) {
		this.member = member;
	}

    
}
