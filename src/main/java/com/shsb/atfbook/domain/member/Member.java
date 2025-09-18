package com.shsb.atfbook.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shsb.atfbook.domain.history.History;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name="MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private long id;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @Column(name = "member_type")
    private String memberType;
    @Column(name="session_id")
    private String sessionId;

    @Column(length = 20, name = "query_type")
    private String queryType;

    @Column(length = 20, name = "filter_type")
    private String fiterType;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<History> histories = new ArrayList<>();

    public static Member register(MemberRegisterRequest createRequest, PasswordManager passwordManager) {
        Member member = new Member();

        member.loginId = createRequest.loginId();
        member.password = passwordManager.hashPassword(createRequest.password());

        return member;
    }
}
