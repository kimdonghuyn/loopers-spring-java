package com.loopers.domain.user;

import com.loopers.support.enums.Gender;

public class UserCommand {

    public record SignUp(
            String loginId,
            String name,
            String email,
            String birth,
            Gender gender
    ) {}
}
