package com.lec.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lec.entity.Reply;

public class ReplyDTO {
    private int replyId;
    private String content;
    private int level;
    private LocalDateTime replyCreate;
    private  LocalDateTime replyModify;
    private Integer parentReId;
    private int voteId;
    private String userId;
    private String userNickname;  // 사용자 닉네임 표시를 위해 추가
    private List<ReplyDTO> children = new ArrayList<>();

    // 기본 생성자
    public ReplyDTO() {}

    // Reply 엔티티로부터 DTO를 생성하는 생성자
    public ReplyDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.content = reply.getContent();
        this.level = reply.getLevel();
        this.replyCreate = reply.getReplyCreate();
        this.replyModify = reply.getReplyModify();
        this.parentReId = reply.getParentReId();
        this.voteId = reply.getVote().getVoteId();
        this.userId = reply.getMember().getUserId();
        this.userNickname = reply.getMember().getNickname();
    }

    // Getter 및 Setter 메소드들
    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public LocalDateTime getReplyCreate() {
        return replyCreate;
    }

    public void setReplyCreate(LocalDateTime replyCreate) {
        this.replyCreate = replyCreate;
    }

    public LocalDateTime getReplyModify() {
        return replyModify;
    }

    public void setReplyModify(LocalDateTime replyModify) {
        this.replyModify = replyModify;
    }

    public int getParentReId() {
        return parentReId;
    }

    public void setParentReId(int parentReId) {
        this.parentReId = parentReId;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public List<ReplyDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ReplyDTO> children) {
        this.children = children;
    }

    public void addChild(ReplyDTO child) {
        this.children.add(child);
    }
}
