package com.loopers.interfaces.api.point;

import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    private final PointService pointService;

    public PointV1Controller(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getUserPoint(@RequestHeader(value = "X-USER-ID", required = true) String userId) {
        return pointService.getUserPoint(userId);
    }
}
