package com.shsb.atfbook.application.like;

import com.shsb.atfbook.application.category.CategoryRepository;
import com.shsb.atfbook.domain.category.Category;
import com.shsb.atfbook.domain.like.Like;
import com.shsb.atfbook.domain.shared.event.EventType;
import com.shsb.atfbook.domain.shared.event.payload.BookLikedEventPayload;
import com.shsb.atfbook.domain.shared.messagerelay.OutboxEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public void like(Like like){
        Optional<Like> result = likeRepository
                .findByItemIdAndLoginId(like.getItemId(), like.getLoginId())
                .map(existing -> {
                    likeRepository.deleteById(existing.getLikeId());
                    return Optional.<Like>empty();
                })
                .orElseGet(() -> Optional.of(likeRepository.save(like)));

        outboxEventPublisher.publish(
                result.isPresent() ? EventType.BOOK_LIKED : EventType.BOOK_UNLIKED,
                BookLikedEventPayload.builder()
                        .itemId(like.getItemId())
                        .bookLikeId(result.isPresent() ? result.get().getLikeId() : like.getLikeId())
                        .loginId(like.getLoginId())
                        .build(),
                Long.valueOf(like.getItemId())
        );
    }
}
