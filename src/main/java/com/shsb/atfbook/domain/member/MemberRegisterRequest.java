package com.shsb.atfbook.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public record MemberRegisterRequest(
        @NotBlank String loginId,
        @NotBlank String password) {
}
