package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeCriteria;
import com.loopers.application.like.LikeResult;
import jakarta.validation.constraints.NotNull;

public class LikeV1Dto {
    public record LikeRequest(
            @NotNull
            String loginId,
            @NotNull
            Long productId
    ) {
        public LikeCriteria.Like  toCriteria(LikeV1Dto.LikeRequest request) {
            return new LikeCriteria.Like(request.loginId(), request.productId);
        }
    }

    public record LikeResponse(
            @NotNull
            Long id,
            @NotNull
            Long userId,
            @NotNull
            Long productId
    ) {
        public static LikeResponse from(LikeResult likeResult) {
            return new LikeResponse(
                    likeResult.id(),
                    likeResult.userId(),
                    likeResult.productId()
            );
        }
    }
}
