package com.loopers.domain.point;

import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserService userService;

    public ApiResponse<PointV1Dto.PointResponse> getUserPoint(String userId) {

        var userInfo = userService.getUserInfo(userId);

        PointV1Dto.PointResponse response = new PointV1Dto.PointResponse("loopers123", 100L);

        return ApiResponse.success(response);
    }

    public ApiResponse<PointV1Dto.PointResponse> chargePoint(String userId, Long chargePointAmount) throws CoreException {
        UserV1Dto.UserResponse userInfo = userService.getUserInfo(userId);

        ApiResponse<PointV1Dto.PointResponse> userPoint = getUserPoint(userId);

        PointV1Dto.PointResponse response = new PointV1Dto.PointResponse(
                userInfo.userId(),
                userPoint.data().point() + chargePointAmount
        );
        return ApiResponse.success(response);
    }
}
