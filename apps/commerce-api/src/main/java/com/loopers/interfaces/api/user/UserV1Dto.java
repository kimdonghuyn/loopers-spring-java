package com.loopers.interfaces.api.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {

    public record SignUpRequest(
            @NotNull
            String userId,
            @NotNull
            String name,
            @NotNull(message = "성별은 필수입니다.")
            GenderRequest gender,
            @NotNull
            String email,
            @NotNull
            String birth
    ) {
        enum GenderRequest {
            M,
            F
        }
    }

    public record UserResponse(
            String userId,
            String name,
            GenderResponse gender,
            String email,
            String birth
    ) {

    }

    enum GenderResponse {
        M,
        F
    }
}
