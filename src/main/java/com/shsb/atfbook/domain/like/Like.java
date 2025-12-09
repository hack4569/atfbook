package com.shsb.atfbook.domain.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="like_book")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    @Column(nullable = false)
    private int itemId;
    @Column(nullable = false)
    private String loginId;
    @CreatedDate
    private LocalDateTime created;

    public static Like create(int itemId, String loginId) {
        Like like = new Like();
        like.setLoginId(loginId);
        like.setItemId(itemId);
        return like;
    }
}
