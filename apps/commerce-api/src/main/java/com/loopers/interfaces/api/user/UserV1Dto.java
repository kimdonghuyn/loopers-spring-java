package com.loopers.interfaces.api.user;

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

    }
}
