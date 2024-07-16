package com.lec.repository;

import com.lec.entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {

    void deleteByMemberUserId(String userId);
}
