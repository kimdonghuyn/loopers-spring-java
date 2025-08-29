package com.loopers.application.like;

public record LikeEvent(Long productId, boolean isLike) {
}
