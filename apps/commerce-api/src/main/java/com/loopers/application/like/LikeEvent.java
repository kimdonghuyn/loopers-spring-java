package com.loopers.application.like;

public record LikeEvent(String loginId, Long productId, boolean isLike) {
}
