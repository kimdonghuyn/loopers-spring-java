package com.loopers.domain.like;

public interface LikeRepository {
    void save(LikeEntity likeEntity);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(LikeEntity likeEntity);
}
