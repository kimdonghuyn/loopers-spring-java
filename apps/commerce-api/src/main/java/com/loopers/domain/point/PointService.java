package com.loopers.domain.point;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.Gender;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final UserService userService;

    public Optional<ApiResponse<PointEntity>> getUserPoint(String userId) {
        UserEntity userInfo = new UserEntity(
                "loopers123",
                "hyun",
                Gender.F,
                "say3356@naver.com",
                "2002-10-10"
        );

        Long point = 0L;

        if (!userInfo.getUserId().equals(userId)) {
            return null;
        } else {
            point = 100L;
        }

        PointEntity response = new PointEntity(userInfo.getUserId(), point);

        return Optional.of(ApiResponse.success(response));
    }

    public ApiResponse<PointEntity> charge(String userId, Long chargePointAmount) throws CoreException {
        UserEntity userInfo = userService.getUserInfo(userId);

        Optional<ApiResponse<PointEntity>> userPoint = getUserPoint(userId);

        PointEntity response = new PointEntity(
                userInfo.getUserId(),
                userPoint.get().data().getPoint() + chargePointAmount
        );
        return ApiResponse.success(response);
    }
}
