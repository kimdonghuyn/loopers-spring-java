package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class UserEntity {
    private String userId;
    private String name;
    private String email;
    private String birth;

    private final String PATTERN_USER_ID = "^[a-zA-Z0-9]{1,10}$";
    private final String PATTERN_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String PATTERN_BIRTH = "^\\d{4}-\\d{2}-\\d{2}$";

    public UserEntity(String userId, String name, String email, String birth) {
        if (userId == null || !userId.matches(PATTERN_USER_ID)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 영문 및 숫자 10자 이내로 입력해야 합니다.");
        }

        if (email == null || !email.matches(PATTERN_EMAIL)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 xx@yy.zz 형식으로 입력해야 합니다.");
        }

        if (birth == null || !birth.matches(PATTERN_BIRTH)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-mm-dd 형식으로 입력해야 합니다.");
        }
    }
}
