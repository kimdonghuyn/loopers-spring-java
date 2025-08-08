package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void like(LikeCommand.Like likeCommand) {
        try {
            likeRepository.save(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
        } catch (DataIntegrityViolationException ignored) {
        }
    }

    @Transactional
    public void unlike(LikeCommand.Like likeCommand) {
        likeRepository.deleteByUserIdAndProductId(likeCommand.userId(), likeCommand.productId());
    }

    private boolean isExistLike(LikeCommand.Like likeCommand) {
        return likeRepository.existsByUserIdAndProductId(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
    }
}
