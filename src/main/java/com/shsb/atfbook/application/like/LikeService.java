package com.shsb.atfbook.application.like;

import com.shsb.atfbook.application.category.CategoryRepository;
import com.shsb.atfbook.domain.category.Category;
import com.shsb.atfbook.domain.like.Like;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void like(Like like){
        likeRepository.findById(like.getLikeId()).ifPresentOrElse(
                existing -> likeRepository.deleteById(existing.getLikeId()),
                () -> likeRepository.save(like)
        );
    }
}
