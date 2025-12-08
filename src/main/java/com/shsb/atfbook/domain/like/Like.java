package com.shsb.atfbook.domain.like;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name="like")
public class Like {
    @Id
    private Long likeId;
    private int itemId;
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
