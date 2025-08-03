package com.loopers.application.like;

import com.loopers.domain.like.LikeCommand;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.user.LoginId;

public class LikeCriteria {
    public record Like(
            String loginId,
            Long productId
    ) {
        public LikeCommand.Like toCommand() {
            return new LikeCommand.Like(loginId, productId);
        }
    }
}
