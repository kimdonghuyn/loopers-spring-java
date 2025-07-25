package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.support.Gender;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {

    public record SignUpRequest(
            @NotNull
            String userId,
            @NotNull
            String name,
            @NotNull(message = "성별은 필수입니다.")
            Gender gender,
            @NotNull
            String email,
            @NotNull
            String birth
    ) {
    }

    public record UserResponse(
            String userId,
            String name,
            Gender gender,
            String email,
            String birth
    ) {
        public static UserV1Dto.UserResponse from(UserEntity info) {
            return new UserV1Dto.UserResponse(
                    info.getUserId(),
                    info.getName(),
                    info.getGender(),
                    info.getEmail(),
                    info.getBirth()
            );
        }
    }

}
