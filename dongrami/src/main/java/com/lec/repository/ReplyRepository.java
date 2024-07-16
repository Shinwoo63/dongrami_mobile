package com.lec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.lec.entity.Reply;

import jakarta.transaction.Transactional;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    

	@Query("SELECT r FROM Reply r WHERE r.vote.voteId = :voteId AND r.level = :level ORDER BY r.replyCreate DESC")
    Page<Reply> findByVoteId(@Param("voteId") int voteId, @Param("level") int level, Pageable pageable);
	
	@Query("SELECT r FROM Reply r WHERE r.parentReId= :parentReId ORDER BY r.replyCreate DESC")
	Page<Reply> findByParentReId(@Param("parentReId") int parentReId, Pageable pageable);

	    @Transactional
    void deleteByMemberUserId(String userId);
}
