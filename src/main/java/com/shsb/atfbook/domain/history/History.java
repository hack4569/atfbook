package com.shsb.atfbook.domain.history;

import com.shsb.atfbook.domain.BaseEntity;
import com.shsb.atfbook.domain.member.Member;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@Table(name="histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "item_id")
    private int itemId;

    public void setMember(Member member) {
        this.member = member;
        member.getHistories().add(this);
    }

    public static History createHistory(int bookId, Member member) {
        History history = new History();
        history.setItemId(bookId);
        history.setMember(member);
        return history;
    }
}