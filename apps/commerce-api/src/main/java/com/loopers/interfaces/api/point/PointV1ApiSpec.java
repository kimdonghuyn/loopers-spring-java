package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;

@Tag(name = "User V1 API", description = "회원가입 및 사용자 정보 조회 API")
public interface PointV1ApiSpec {
    ApiResponse<PointV1Dto.GetResponse> getUserPoint(String userId) throws CoreException;

    ApiResponse<PointV1Dto.GetResponse> charge(String userId, BigDecimal amount) throws CoreException;
}
