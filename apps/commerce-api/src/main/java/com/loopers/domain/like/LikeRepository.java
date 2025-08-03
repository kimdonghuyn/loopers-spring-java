package com.loopers.domain.like;

public interface LikeRepository {
    void save(LikeEntity likeEntity);

    void deleteByProductId(LikeEntity likeEntity);

    boolean existsByUserIdAndProductId(LikeEntity likeEntity);
}
