package com.loopers.domain.user;

import com.loopers.support.enums.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class UserValidator {
    private static final String PATTERN_USER_ID = "^[a-zA-Z0-9]{1,10}$";
    private static final String PATTERN_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PATTERN_BIRTH = "^\\d{4}-\\d{2}-\\d{2}$";

    public static void validateUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 필수 입력값입니다.");
        } else if (!userId.matches(PATTERN_USER_ID)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 영문 및 숫자 10자 이내로 입력해야 합니다.");
        }
    }
    public static void validateEmail(String email) {
        if (email == null || !email.matches(PATTERN_EMAIL)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 xx@yy.zz 형식으로 입력해야 합니다.");
        }
    }
    public static void validateBirth(String birth) {
        if (birth == null || !birth.matches(PATTERN_BIRTH)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-mm-dd 형식으로 입력해야 합니다.");
        }
    }

    public static void validateGender(Gender gender) {
        if (gender == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "성별은 필수 입력값입니다.");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이름은 필수 입력값입니다.");
        }
    }
}
