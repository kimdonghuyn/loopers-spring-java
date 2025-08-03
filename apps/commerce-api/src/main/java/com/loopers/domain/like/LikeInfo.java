package com.loopers.domain.like;

import com.loopers.application.like.LikeResult;

public record LikeInfo(
        Long id,
        String userId,
        Long productId
) {
    public static LikeInfo from(LikeEntity likeEntity) {
        return new LikeInfo(
                likeEntity.getId(),
                likeEntity.getUserId().getLoginId(),
                likeEntity.getProductId()
        );
    }
}
