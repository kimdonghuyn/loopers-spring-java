package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointCriteria;
import com.loopers.application.point.PointFacade;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;
    private final PointService pointService;

    public PointV1Controller(PointFacade pointFacade, PointService pointService) {
        this.pointFacade = pointFacade;
        this.pointService = pointService;
    }

    @GetMapping("")
    @Override
    public ApiResponse<PointV1Dto.GetResponse> getUserPoint(@RequestHeader(value = "X-USER-ID", required = true) String loginId) throws CoreException {

        PointV1Dto.GetResponse response = PointV1Dto.GetResponse.from(
                pointFacade.get(loginId)
        );

        return ApiResponse.success(response);
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.GetResponse> charge(@RequestHeader(value = "X-USER-ID", required = true) String loginId, @RequestBody Long amount) throws CoreException {

        PointV1Dto.GetResponse response = PointV1Dto.GetResponse.from(
                pointFacade.charge(new PointCriteria.Charge(loginId, amount))
        );

        return ApiResponse.success(response);
    }
}
