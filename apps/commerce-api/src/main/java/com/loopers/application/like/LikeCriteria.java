package com.loopers.application.like;

import com.loopers.domain.like.LikeCommand;

public class LikeCriteria {
    public record Like(
            String loginId,
            Long productId
    ) {
        public LikeCommand.Like toCommand(Long userId) {
            return new LikeCommand.Like(userId, productId);
        }
    }
}
