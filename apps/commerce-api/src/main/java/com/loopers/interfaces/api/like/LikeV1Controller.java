package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeCriteria;
import com.loopers.application.like.LikeFacade;
import com.loopers.application.like.LikeResult;
import com.loopers.application.point.PointCriteria;
import com.loopers.application.point.PointFacade;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1ApiSpec;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
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
    public ApiResponse<LikeV1Dto.LikeResponse> like(
            @RequestHeader(value = "X-USER-ID", required = true) String loginId,
            @PathVariable Long productId
    ) {
        LikeV1Dto.LikeRequest likeRequest = new LikeV1Dto.LikeRequest(loginId, productId);
        LikeResult likeResult = likeFacade.like(likeRequest.toCriteria(likeRequest));
        return ApiResponse.success(LikeV1Dto.LikeResponse.from(likeResult));
    }

    @Override
    @DeleteMapping("/{productId}")
    public ApiResponse<?> unlike(
            @RequestHeader(value = "X-USER-ID", required = true) String loginId,
            @PathVariable Long productId
    ) {
        LikeV1Dto.LikeRequest likeRequest = new LikeV1Dto.LikeRequest(loginId, productId);
        likeFacade.like(likeRequest.toCriteria(likeRequest));

        return  ApiResponse.success();
    }
}
