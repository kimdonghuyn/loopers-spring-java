package com.loopers.domain.like;

public record LikeInfo(
        Long id,
        Long userId,
        Long productId
) {
    public static LikeInfo from(LikeEntity likeEntity) {
        return new LikeInfo(
                likeEntity.getId(),
                likeEntity.getUserId(),
                likeEntity.getProductId()
        );
    }
}
