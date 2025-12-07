package com.shsb.atfbook.application.user;

import com.shsb.atfbook.application.member.MemberRepository;
import com.shsb.atfbook.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId){
        return memberRepository.findMemberByLoginId(loginId).orElse(null);
    }

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
