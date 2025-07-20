package com.loopers.interfaces.api.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
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
        PointEntity pointInfo = pointService.getUserPoint(userId).get().data();

        PointV1Dto.PointResponse response = PointV1Dto.PointResponse.from(pointInfo);

        return ApiResponse.success(response);
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> charge(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestBody PointV1Dto.PointRequest chargePointRequest) throws CoreException {
        PointEntity pointInfo = pointService.charge(userId, chargePointRequest.chargePointAmount()).data();

        PointV1Dto.PointResponse response = PointV1Dto.PointResponse.from(pointInfo);

        return ApiResponse.success(response);
    }


}
