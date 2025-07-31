package com.loopers.interfaces.api.like;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like V1 API", description = "좋아요 등록 및 취소 API")
public interface LikeV1ApiSpec {
    void like(String loginId, Long productId);

    void unlike(String loginId, Long productId);
}
