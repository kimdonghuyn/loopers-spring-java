package com.loopers.domain.user;

import com.loopers.support.Gender;

public class UserCommand {

    public record SignUp(
            String loginId,
            String name,
            String email,
            String birth,
            Gender gender
    ) {}
}
