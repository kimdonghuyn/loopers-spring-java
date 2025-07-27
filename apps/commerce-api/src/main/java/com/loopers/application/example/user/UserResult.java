package com.loopers.application.example.user;

import com.loopers.domain.user.UserInfo;
import com.loopers.support.Gender;

public record UserResult(
        Long id,
        String loginId,
        String name,
        String email,
        String birth,
        Gender gender
) {
    public static UserResult from(final UserInfo userInfo) {
        return new UserResult(
                userInfo.id(),
                userInfo.loginId().getLoginId(),
                userInfo.name(),
                userInfo.email().getEmail(),
                userInfo.birth().getBirth(),
                userInfo.gender()
        );
    }
}

