package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like/products")
public class LikeV1Controller implements LikeV1ApiSpec {

    private final LikeFacade likeFacade;

    public LikeV1Controller(LikeFacade likeFacade) {
        this.likeFacade = likeFacade;
    }

    @Override
    @PostMapping("/{productId}")
    public ApiResponse<?> like(
            @RequestHeader(value = "X-USER-ID", required = true) String loginId,
            @PathVariable Long productId
    ) {
        LikeV1Dto.LikeRequest likeRequest = new LikeV1Dto.LikeRequest(loginId, productId);
        likeFacade.like(likeRequest.toCriteria(likeRequest));
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/{productId}")
    public ApiResponse<?> unlike(
            @RequestHeader(value = "X-USER-ID", required = true) String loginId,
            @PathVariable Long productId
    ) {
        LikeV1Dto.LikeRequest likeRequest = new LikeV1Dto.LikeRequest(loginId, productId);
        likeFacade.unlike(likeRequest.toCriteria(likeRequest));

        return  ApiResponse.success();
    }
}
