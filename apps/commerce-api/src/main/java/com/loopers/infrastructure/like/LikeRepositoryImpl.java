package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEntity;
import com.loopers.domain.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;

    @Override
    public void save(LikeEntity likeEntity) {
        likeJpaRepository.save(likeEntity);
    }

    @Override
    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        likeJpaRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean existsByUserIdAndProductId(LikeEntity likeEntity) {
        return likeJpaRepository.existsByUserIdAndProductId(likeEntity.getUserId(), likeEntity.getProductId());
    }
}
