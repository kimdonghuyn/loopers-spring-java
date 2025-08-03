package com.loopers.application.like;

import com.loopers.domain.like.LikeInfo;

public record LikeResult(
        Long id,
        String userId,
        Long productId
) {
    public static LikeResult from(LikeInfo likeInfo) {
        return new LikeResult(
                likeInfo.id(),
                likeInfo.userId(),
                likeInfo.productId()
        );
    }
}
