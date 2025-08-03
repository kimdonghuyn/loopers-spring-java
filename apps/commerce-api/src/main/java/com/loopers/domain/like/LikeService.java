package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void like(LikeCommand.Like likeCommand) {
        if (!isExistLike(likeCommand)) {
            likeRepository.save(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
        }
    }

    public void unlike(LikeCommand.Like likeCommand) {
        likeRepository.deleteByProductId(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
    }

    private boolean isExistLike(LikeCommand.Like likeCommand) {
        return likeRepository.existsByUserIdAndProductId(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
    }
}
