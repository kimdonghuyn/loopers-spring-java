package com.loopers.domain.point;

import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.Gender;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserService userService;

    public ApiResponse<PointV1Dto.PointResponse> getUserPoint(String userId) {
        UserV1Dto.UserResponse userInfo = new UserV1Dto.UserResponse(
                "loopers123",
                "hyun",
                Gender.F,
                "say3356@naver.com",
                "2002-10-10"
        );

        Long point = 0L;

        if (!userInfo.userId().equals(userId)) {
            point = null;
        } else {
            point = 100L;
        }

        PointV1Dto.PointResponse response = new PointV1Dto.PointResponse(userInfo.userId(), point);

        return ApiResponse.success(response);
    }

    public ApiResponse<PointV1Dto.PointResponse> charge(String userId, Long chargePointAmount) throws CoreException {
        UserV1Dto.UserResponse userInfo = userService.getUserInfo(userId);

        ApiResponse<PointV1Dto.PointResponse> userPoint = getUserPoint(userId);

        PointV1Dto.PointResponse response = new PointV1Dto.PointResponse(
                userInfo.userId(),
                userPoint.data().point() + chargePointAmount
        );
        return ApiResponse.success(response);
    }
}
