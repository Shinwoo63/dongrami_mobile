package com.lec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.entity.Vote;
import com.lec.repository.VoteRepository;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    // 모든 투표 리스트 조회
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    // 특정 ID의 투표 조회
    public Optional<Vote> getVoteById(int voteId) {
        return voteRepository.findById(voteId);
    }

    // 투표 생성
    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
    }

    // 투표 업데이트
    public Vote updateVote(Vote vote) {
        return voteRepository.save(vote);
    }

    // 투표 삭제
    public void deleteVote(int id) {
        voteRepository.deleteById(id);
    }
    // 페이징
    public Page<Vote> getPagedVotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return voteRepository.findAll(pageable);
    }
}
