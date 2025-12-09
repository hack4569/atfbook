package com.shsb.atfbook.application.like;

import com.shsb.atfbook.domain.history.History;
import com.shsb.atfbook.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByItemIdAndLoginId(int itemId, String loginId);
}
