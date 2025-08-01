package com.loopers.domain.product;

public record ProductWithLikeCount(
        ProductEntity product,
        Long likeCount
) {
}
