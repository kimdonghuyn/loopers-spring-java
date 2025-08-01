package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like V1 API", description = "좋아요 등록 및 취소 API")
public interface LikeV1ApiSpec {
    ApiResponse<?> like(String loginId, Long productId);

    ApiResponse<?> unlike(String loginId, Long productId);
}
