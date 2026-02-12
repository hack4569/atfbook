package com.shsb.atfbook.domain.shared.event;

import com.shsb.atfbook.domain.shared.event.payload.BookLikedEventPayload;
import com.shsb.atfbook.domain.shared.event.payload.BookUnLikedEventPayload;
import com.shsb.atfbook.domain.shared.event.payload.EventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    BOOK_LIKED(BookLikedEventPayload.class, Topic.BOOK_LIKED),
    BOOK_UNLIKED(BookUnLikedEventPayload .class, Topic.BOOK_UNLIKED);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String BOOK_LIKED = "book_liked";
        public static final String BOOK_UNLIKED = "book_unliked";
    }
}
