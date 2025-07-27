package com.loopers.interfaces.api.user;

import com.loopers.application.example.user.UserCriteria;
import com.loopers.application.example.user.UserResult;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.Gender;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {

    public record SignUpRequest(
            @NotNull
            String loginId,
            @NotNull
            String name,
            @NotNull
            String email,
            @NotNull
            String birth,
            @NotNull(message = "성별은 필수입니다.")
            Gender gender
    ) {
        public UserCriteria.SignUp toCriteria() {
            return new UserCriteria.SignUp(loginId, name, email, birth, gender);
        }
    }

    public record UserResponse(
            Long id,
            String loginId,
            String name,
            String email,
            String birth,
            Gender gender
            ) {
        public static UserResponse from(final UserResult userResult) {
            return new UserResponse(
                    userResult.id(),
                    userResult.loginId(),
                    userResult.name(),
                    userResult.email(),
                    userResult.email(),
                    userResult.gender()
            );
        }
    }

}
