package com.lec.repository;

import com.lec.entity.SavedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedResultRepository extends JpaRepository<SavedResult, Integer> {
    // 추가된 메서드
    Optional<SavedResult> findById(Integer resultId);

	void deleteByMemberUserId(String userId);
}


// 원래는 string->integer로 바뀜 
