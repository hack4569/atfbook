package com.shsb.atfbook.application.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class MemberAddForm {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;
}
