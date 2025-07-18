package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "회원가입 및 사용자 정보 조회 API")
public interface UserV1ApiSpec {
    ApiResponse<UserV1Dto.UserResponse> signUp(UserV1Dto.SignUpRequest signUpRequest);

    ApiResponse<UserV1Dto.UserResponse> getUserInfo(String userId) throws CoreException;
}
