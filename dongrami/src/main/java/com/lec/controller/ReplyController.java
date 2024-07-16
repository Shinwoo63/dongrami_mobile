package com.lec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lec.dto.ReplyDTO;
import com.lec.entity.Reply;
import com.lec.entity.Vote;
import com.lec.service.ReplyService;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping
    public List<Reply> getAllReplies() {
        return replyService.getAllReplies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reply> getReplyById(@PathVariable int id) {
        Reply reply = replyService.getReplyById(id);
        return reply != null ? ResponseEntity.ok(reply) : ResponseEntity.notFound().build();
    }
    // 특정 부모 댓글 ID에 대한 모든 답글 조회
    @GetMapping("/{parentReId}/reply")
    public Page<Reply> getRepliesByParentReId(@PathVariable("parentReId")int parentReId , Pageable pageable) {
        return replyService.getRepliesByParentReId(parentReId, pageable);
    }
    @GetMapping("/{voteId}/replies")
    public Page<Reply> getRepliesByVoteId(@PathVariable("voteId") int voteId,  @RequestParam(value = "level", defaultValue = "1") int level, Pageable pageable ) {
        return replyService.getRepliesByVoteId(voteId, level, pageable);
    }
    @PostMapping("/{voteId}")
    public ResponseEntity<Reply> addReplyToVote(
            @PathVariable("voteId") int voteId,
            @RequestBody Reply newReply
            ) {
        // 특정 투표에 댓글 추가하기
        Reply savedReply = replyService.addReplyToVote(voteId, newReply);

        // 새로 작성된 댓글과 함께 201 Created 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }


    @PutMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(@PathVariable("replyId") int replyId, @RequestBody Reply replyDetails) {
        Reply updatedReply = replyService.updateReply(replyId, replyDetails);
        return updatedReply != null ? ResponseEntity.ok(updatedReply) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable("replyId") int replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }
}
