package com.shsb.atfbook.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordManager {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    // 비밀번호를 해싱하는 메소드
    public String hashPassword(String plainTextPassword) {
        return encoder.encode(plainTextPassword);
    }

    // 저장된 해시와 사용자가 입력한 비밀번호가 일치하는지 검증하는 메소드
    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // BCrypt.checkpw()를 사용하여 비밀번호가 일치하는지 확인
        return encoder.matches(plainTextPassword, hashedPassword);
    }
}
