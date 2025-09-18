package com.shsb.atfbook.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        String loginId,
        String password) {
}
