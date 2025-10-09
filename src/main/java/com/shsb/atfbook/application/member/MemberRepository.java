package com.shsb.atfbook.application.member;

import com.shsb.atfbook.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByLoginId(String loginId);
    Optional<Member> findMemberBySessionId(String sessionId);
}
