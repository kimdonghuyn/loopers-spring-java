package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.support.enums.Gender;

public class UserCriteria {

    public record SignUp(
            String loginId,
            String name,
            String email,
            String birth,
            Gender gender
    ) {
        public UserCommand.SignUp toCommand() {
            return new UserCommand.SignUp(loginId, name, email, birth, gender);
        }
    }
}
