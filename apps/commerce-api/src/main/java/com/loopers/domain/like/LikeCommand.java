package com.loopers.domain.like;

public class LikeCommand {
    public record Like(
            Long userId,
            Long productId
    ) {}
}
