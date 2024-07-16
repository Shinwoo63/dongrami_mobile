package com.lec.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.dto.ReplyDTO;
import com.lec.entity.Reply;
import com.lec.entity.Vote;
import com.lec.repository.ReplyRepository;
import com.lec.repository.VoteRepository;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    
    @Autowired
    private VoteRepository voteRepository;

    public List<Reply> getAllReplies() {
    	return replyRepository.findAll();
    }
    public Reply getReplyById(int id) {
        return replyRepository.findById(id).orElse(null);
    }
   public Page<Reply> getRepliesByParentReId(int parentReId, Pageable pageable) {
        return replyRepository.findByParentReId(parentReId, pageable);
    }
    public Page<Reply> getRepliesByVoteId(int voteId, int level, Pageable pageable) {
        return replyRepository.findByVoteId(voteId, level, pageable);
    }

    public ReplyDTO createReply(ReplyDTO replyDTO) {
        Reply reply = new Reply(
                replyDTO.getReplyId(),
                replyDTO.getContent(),
                replyDTO.getLevel(),
                replyDTO.getReplyCreate(),
                replyDTO.getReplyModify(),
                replyDTO.getParentReId(),
                // vote와 member는 이 예제에서 단순화를 위해 null로 설정
                null, 
                null
        );
        reply = replyRepository.save(reply);
        return new ReplyDTO(reply);
    }
    public Reply addReplyToVote(int voteId, Reply newReply) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("Vote not found"));
        newReply.setVote(vote);
        
        return replyRepository.save(newReply);
    }
    public Reply updateReply(int replyId, Reply replyDetails) {
        Optional<Reply> optionalReply = replyRepository.findById(replyId);
        if (optionalReply.isPresent()) {
            Reply existingReply = optionalReply.get();
            existingReply.setContent(replyDetails.getContent());
            existingReply.setReplyModify(replyDetails.getReplyModify());
            return replyRepository.save(existingReply);
        } else {
            return null;
        }
    }

    public void deleteReply(int replyId) {
        replyRepository.deleteById(replyId);
    }

}
