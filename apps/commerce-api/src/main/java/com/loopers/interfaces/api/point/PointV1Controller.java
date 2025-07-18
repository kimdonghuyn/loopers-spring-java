package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    @GetMapping("")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getUserPoint(@RequestHeader(value = "X-USER-ID", required = true) String userId) {
        return ApiResponse.success(
                new PointV1Dto.PointResponse(
                        userId,
                        1000L
                )
        );
    }
}
