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
    public void deleteByProductId(LikeEntity likeEntity) {
        likeJpaRepository.deleteById(likeEntity.getProductId());
    }
}
