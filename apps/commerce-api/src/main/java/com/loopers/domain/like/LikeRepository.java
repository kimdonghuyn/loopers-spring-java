package com.loopers.domain.like;

public interface LikeRepository {
    LikeEntity save(LikeEntity likeEntity);

    void deleteByProductId(LikeEntity likeEntity);
}
