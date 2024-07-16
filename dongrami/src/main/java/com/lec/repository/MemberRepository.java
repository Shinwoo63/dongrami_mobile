package com.lec.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
  
    // 기존 메서드
    Member findByNickname(String nickname);
    Optional<Member> findByUserId(String userId);
    Member findByEmail(String email);
    Member findByEmailAndProvider(String email, String provider);
    boolean existsByEmail(String email);
    List<Member> findAllByNickname(String nickname);
}
