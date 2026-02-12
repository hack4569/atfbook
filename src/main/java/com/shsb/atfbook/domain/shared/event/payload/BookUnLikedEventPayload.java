package com.shsb.atfbook.domain.shared.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookUnLikedEventPayload implements EventPayload {
    private Long bookLikeId;
    private int itemId;
    private String loginId;
    @CreatedDate
    private LocalDateTime created;
}
